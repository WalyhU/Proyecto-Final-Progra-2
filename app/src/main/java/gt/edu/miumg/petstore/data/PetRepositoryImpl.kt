package gt.edu.miumg.petstore.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.repository.PetRepository
import gt.edu.miumg.petstore.util.Constants.COLLECTION_ACCESSORIES
import gt.edu.miumg.petstore.util.Constants.COLLECTION_FOOD
import gt.edu.miumg.petstore.util.Constants.COLLECTION_PETS
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : PetRepository {
    override fun getPet(): Flow<Response<PetState>> = callbackFlow {
        Response.Loading
        val snapShotListener = firebaseFirestore.collection(COLLECTION_PETS)
            .addSnapshotListener { value, error ->
                val response = if (value != null) {
                    Log.d("PetRepositoryImpl", "getPet: ${value.toObjects(PetState::class.java)}")
                    val petInfo = value.toObjects(PetState::class.java)
                    Response.Success(petInfo)
                } else {
                    Response.Error(error?.message!!)
                }
                trySend(response).isSuccess
            }
        awaitClose { snapShotListener.remove() }
    }

    override fun getFood(): Flow<Response<PetState>> = callbackFlow {
        Response.Loading
        val snapShotListener = firebaseFirestore.collection(COLLECTION_FOOD)
            .addSnapshotListener { value, error ->
                val response = if (value != null) {
                    Log.d("PetRepositoryImpl", "getFood: ${value.toObjects(PetState::class.java)}")
                    val foodInfo = value.toObjects(PetState::class.java)
                    Response.Success(foodInfo)
                } else {
                    Response.Error(error?.message!!)
                }
                trySend(response).isSuccess
            }
        awaitClose { snapShotListener.remove() }

    }

    override fun getAccessory(): Flow<Response<PetState>> = callbackFlow {
        Response.Loading
        val snapShotListener = firebaseFirestore.collection(COLLECTION_ACCESSORIES)
            .addSnapshotListener { value, error ->
                val response = if (value != null) {
                    Log.d("PetRepositoryImpl", "getAccessory: ${value.toObjects(PetState::class.java)}")
                    val accessoryInfo = value.toObjects(PetState::class.java)
                    Response.Success(accessoryInfo)
                } else {
                    Response.Error(error?.message!!)
                }
                trySend(response).isSuccess
            }
        awaitClose { snapShotListener.remove() }
    }
}