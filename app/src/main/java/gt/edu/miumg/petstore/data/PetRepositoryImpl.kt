package gt.edu.miumg.petstore.data

import com.google.firebase.firestore.FirebaseFirestore
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.repository.PetRepository
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
                    val petInfo = value.toObjects(PetState::class.java)
                    Response.Success<PetState>(petInfo!!)
                } else {
                    Response.Error(error?.message!!)
                }
                trySend(response).isSuccess
            }
        awaitClose { snapShotListener.remove() }
    }
}