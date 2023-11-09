package gt.edu.miumg.petstore.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import gt.edu.miumg.petstore.models.CartItem
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.repository.CartRepository
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Constants.COLLECTION_CART
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : CartRepository {
    private var operationSuccessful = false
    override fun getCart(userData: UserData): Flow<Response<CartState>> = callbackFlow {
        Response.Loading
        val snapShotListener = firebaseFirestore.collection(COLLECTION_CART)
            .document(userData.userId)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val items = snapshot.data?.mapValues { entry ->
                        val itemMap = entry.value as Map<String, Any?>
                        CartItem(
                            description = itemMap["description"] as String?,
                            image = itemMap["image"] as String?,
                            price = itemMap["price"] as Double?,
                            quantity = itemMap["quantity"] as Long?,
                            title = itemMap["title"] as String?
                        )
                    }
                    val cart = CartState(items as MutableMap<String?, CartItem>)
                    Response.Success(mutableListOf(cart))
                } else {
                    Response.Error(error?.message ?: error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose { snapShotListener.remove() }
    }

    override fun setCart(userData: UserData, cart: CartState): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            val cartObj = mutableMapOf<String?, Any?>()
            cart.items.forEach { (key, value) ->
                cartObj[key] = value
            }
            firebaseFirestore.collection(COLLECTION_CART)
                .document(userData.userId)
                .update(cartObj)
                .addOnSuccessListener {

                }
                .await()
            if (operationSuccessful) {
                Log.d("CartRepositoryImpl", "setCart: $operationSuccessful")
                emit(Response.Success(mutableListOf(true)))
            } else {
                Log.d("CartRepositoryImpl", "Error1: $operationSuccessful")
                emit(Response.Error("Error al actualizar el carrito"))
            }
        } catch (e: Exception) {
            Log.d("CartRepositoryImpl", "Error2: ${e.localizedMessage}")
            Response.Error(e.localizedMessage ?: "Error inesperado")
        }
    }

    override fun deleteElement(userData: UserData, cart: CartState): Flow<Response<Boolean>> =
        flow {
            operationSuccessful = false
            try {
                val cartObj = mutableMapOf<String?, Any?>()
                cart.items.forEach { (key, value) ->
                    // Aquí se puede hacer un if para verificar si el elemento es el que se quiere eliminar
                    // y no agregarlo al objeto
                    if (value.quantity != 0L) {
                        cartObj[key] = value
                    }
                }
                firebaseFirestore.collection(COLLECTION_CART)
                    .document(userData.userId)
                    .update(cartObj)
                    .addOnSuccessListener {
                        operationSuccessful = true
                    }
                    .await()
                if (operationSuccessful) {
                    Log.d("CartRepositoryImpl", "setCart: $operationSuccessful")
                    emit(Response.Success(mutableListOf(true)))
                } else {
                    Log.d("CartRepositoryImpl", "Error1: $operationSuccessful")
                    emit(Response.Error("Error al actualizar el carrito"))
                }
            } catch (e: Exception) {
                Log.d("CartRepositoryImpl", "Error2: ${e.localizedMessage}")
                Response.Error(e.localizedMessage ?: "Error inesperado")
            }
        }
}