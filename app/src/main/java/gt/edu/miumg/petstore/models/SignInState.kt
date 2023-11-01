package gt.edu.miumg.petstore.models

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
