package gt.edu.miumg.petstore.use_cases.CartUseCases

import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.repository.CartRepository
import gt.edu.miumg.petstore.sign_in.UserData
import javax.inject.Inject

class SetCart @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(userData:UserData, cartStaeItem: CartState) =
        repository.setCart(userData, cartStaeItem)
}
