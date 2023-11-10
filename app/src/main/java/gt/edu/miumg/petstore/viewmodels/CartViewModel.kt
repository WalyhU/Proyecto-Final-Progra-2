package gt.edu.miumg.petstore.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.use_cases.CartUseCases.CartUseCases
import gt.edu.miumg.petstore.util.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _getCart = mutableStateOf<Response<CartState>>(Response.Success(null))
    val getCartData: State<Response<CartState>> = _getCart

    private val _setCart = mutableStateOf<Response<Boolean>>(Response.Success(null))
    val setCartData: State<Response<Boolean>> = _setCart

    var showBottomSheet by mutableStateOf(false)

    fun getCartInfo(userData: UserData) {
        viewModelScope.launch {
            cartUseCases.getCart(userData).collect {
                _getCart.value = it
            }
        }
    }

    fun setCartInfo(userData: UserData, cartState: CartState) {
        viewModelScope.launch {
            cartUseCases.setCart(userData, cartState).collect {
                _setCart.value = it
            }
        }
    }
}