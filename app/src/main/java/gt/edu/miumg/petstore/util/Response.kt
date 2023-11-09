package gt.edu.miumg.petstore.util

sealed class Response<out T>{
    object Loading: Response<Nothing>()

    data class Success<out T>(val data: MutableList<@UnsafeVariance T?>?): Response<T>()

    data class Error(val message: String): Response<Nothing>()
}
