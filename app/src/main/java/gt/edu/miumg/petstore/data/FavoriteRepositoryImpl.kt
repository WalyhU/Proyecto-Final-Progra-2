package gt.edu.miumg.petstore.data

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import gt.edu.miumg.petstore.models.FavoriteState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.repository.FavoriteRepository
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Constants.COLLECTION_FAVORITES
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : FavoriteRepository {
    private var operationSuccessful = false
    override fun getFavorites(userData: UserData): Flow<Response<FavoriteState>> = callbackFlow {
        Response.Loading

        // Comprobar si el usuario tiene favoritos
        val favorites = firebaseFirestore.collection(COLLECTION_FAVORITES)
            .document(userData.userId)
            .get()
            .await()
        if (!favorites.exists()) {
            // Si no tiene carrito, crear uno
            val favoritesObj = mutableMapOf<String?, Any?>()
            favoritesObj["item"] = PetState(
                description = "",
                image = "",
                price = 0.0,
                title = "item"
            )
            firebaseFirestore.collection(COLLECTION_FAVORITES)
                .document(userData.userId)
                .set(favoritesObj)
                .addOnSuccessListener {
                    operationSuccessful = true
                }
                .await()

        }

        val snapshotListener = firebaseFirestore.collection(COLLECTION_FAVORITES)
            .document(userData.userId)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val items = snapshot.data?.mapValues { entry ->
                        val itemMap = entry.value as Map<String, Any?>
                        PetState(
                            description = itemMap["description"] as String?,
                            image = itemMap["image"] as String?,
                            price = itemMap["price"] as Double?,
                            title = itemMap["title"] as String?
                        )
                    }
                    val cart = FavoriteState(items as MutableMap<String?, PetState>)
                    Response.Success(mutableListOf(cart))
                } else {
                    Response.Error(error?.message ?: "Error")
                }
                trySend(response).isSuccess
            }
        awaitClose { snapshotListener.remove() }
    }

    override fun setFavorite(userData: UserData, favorite: FavoriteState): Flow<Response<Boolean>> =
        flow {
            operationSuccessful = false
            try {
                val favoritesObj = mutableMapOf<String?, Any?>()
                favorite.items.forEach { (key, value) ->
                    favoritesObj[key] = value
                }
                firebaseFirestore.collection(COLLECTION_FAVORITES)
                    .document(userData.userId)
                    .update(favoritesObj)
                    .addOnSuccessListener {
                    }
                    .await()
                if (operationSuccessful) {
                    emit(Response.Success(mutableListOf(true)))
                } else {
                    emit(Response.Error("Error"))
                }
            } catch (e: Exception) {
                emit(Response.Error(e.message ?: "Error"))
            }
        }

    override fun deleteFavorites(userData: UserData, favorite: PetState): Flow<Response<Boolean>> =
        callbackFlow {
            operationSuccessful = false
            try {
                // Obtener los favoritos del usuario y eliminar el que se desea
                val favorites = firebaseFirestore.collection(COLLECTION_FAVORITES)
                    .document(userData.userId)
                    .get()

                Tasks.whenAllComplete(favorites)
                    .addOnSuccessListener {
                        // Obtener los favoritos del usuario
                        val favoritesObj = mutableMapOf<String?, Any?>()
                        favorites.result?.data?.forEach { (key, value) ->
                            favoritesObj[key] = value
                        }
                        // Eliminar el favorito que se desea
                        favoritesObj?.remove(favorite.title)
                        // Actualizar los favoritos del usuario
                        if (favoritesObj != null) {
                            firebaseFirestore.collection(COLLECTION_FAVORITES)
                                .document(userData.userId)
                                .set(favoritesObj)
                                .addOnSuccessListener {
                                    operationSuccessful = true
                                }
                        }
                        trySend(Response.Success(mutableListOf(operationSuccessful))).isSuccess
                    }
                awaitClose { favorites.isComplete }
            } catch (e: Exception) {
                trySend(Response.Error(e.message ?: "Error")).isSuccess
            }
        }
}