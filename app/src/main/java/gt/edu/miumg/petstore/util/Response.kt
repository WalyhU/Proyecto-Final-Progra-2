package gt.edu.miumg.petstore.util

import gt.edu.miumg.petstore.models.PetState

sealed class Response<out T>{
    object Loading: Response<Nothing>()

    data class Success<out T>(val data: MutableList<PetState>?): Response<T>()

    data class Error(val message: String): Response<Nothing>()
}
