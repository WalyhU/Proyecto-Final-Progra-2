package gt.edu.miumg.petstore.repository

import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCart(userData: UserData): Flow<Response<CartState>>
    fun setCart(userData: UserData, cart: CartState): Flow<Response<Boolean>>
    fun deleteElement(userData: UserData, cart: CartState): Flow<Response<Boolean>>
}