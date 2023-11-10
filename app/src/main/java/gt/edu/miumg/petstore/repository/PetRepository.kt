package gt.edu.miumg.petstore.repository

import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun getPet(): Flow<Response<PetState>>
    fun getFood(): Flow<Response<PetState>>
    fun getAccessory(): Flow<Response<PetState>>
}