package gt.edu.miumg.petstore.data

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.models.SearchState
import gt.edu.miumg.petstore.repository.SearchRepository
import gt.edu.miumg.petstore.util.Constants.COLLECTION_ACCESSORIES
import gt.edu.miumg.petstore.util.Constants.COLLECTION_FOOD
import gt.edu.miumg.petstore.util.Constants.COLLECTION_PETS
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : SearchRepository {
    override fun getAllSearch(query: String): Flow<Response<SearchState>> = callbackFlow {
        Response.Loading
        var petList: MutableList<PetState>
        var foodList: MutableList<PetState>
        var accessoryList: MutableList<PetState>

        val snapShotListenerPets = firebaseFirestore.collection(COLLECTION_PETS)
            .whereArrayContains("keywords", query.lowercase())
            .get()

        val snapShotListenerFood = firebaseFirestore.collection(COLLECTION_FOOD)
            .whereArrayContains("keywords", query.lowercase())
            .get()

        val snapShotListenerAccessories = firebaseFirestore.collection(COLLECTION_ACCESSORIES)
            .whereArrayContains("keywords", query.lowercase())
            .get()

        Tasks.whenAllComplete(snapShotListenerPets, snapShotListenerFood, snapShotListenerAccessories)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    petList = snapShotListenerPets.result?.toObjects(PetState::class.java) ?: mutableListOf()
                    foodList = snapShotListenerFood.result?.toObjects(PetState::class.java) ?: mutableListOf()
                    accessoryList = snapShotListenerAccessories.result?.toObjects(PetState::class.java) ?: mutableListOf()

                    val response = Response.Success(mutableListOf(SearchState(query, petList, foodList, accessoryList)))
                    trySend(response).isSuccess
                } else {
                    val response = Response.Error(task.exception?.message!!)
                    trySend(response).isSuccess
                }
            }
        awaitClose {
            snapShotListenerPets.isSuccessful
            snapShotListenerFood.isSuccessful
            snapShotListenerAccessories.isSuccessful
        }
    }

    override fun getSearchByCollection(
        collection: String,
        query: String
    ): Flow<Response<SearchState>> = callbackFlow {
        Response.Loading
        val snapShotListener = firebaseFirestore.collection(collection)
            .addSnapshotListener { value, error ->
                val response = if (value != null) {
                    Log.d("PetRepositoryImpl", "getPet: ${value.toObjects(PetState::class.java)}")
                    val petInfo = value.toObjects(SearchState::class.java)
                    Response.Success(petInfo!!)
                } else {
                    Response.Error(error?.message!!)
                }
                trySend(response).isSuccess
            }
        awaitClose { snapShotListener.remove() }
    }
}