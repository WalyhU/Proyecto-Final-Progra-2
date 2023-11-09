package gt.edu.miumg.petstore.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.components.AddToCartAnimation
import gt.edu.miumg.petstore.components.Cards
import gt.edu.miumg.petstore.components.CartItem
import gt.edu.miumg.petstore.components.SuccessDialog
import gt.edu.miumg.petstore.models.CartItem
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import gt.edu.miumg.petstore.viewmodels.CartViewModel
import gt.edu.miumg.petstore.viewmodels.PetViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCatalog(
    userData: UserData,
    navController: NavController,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val cartViewModel: CartViewModel = hiltViewModel()
    val openBuySuccess = remember { mutableStateOf(false) }
    cartViewModel.getCartInfo(userData)
    // Test para extraer datos de firebase
    val petviewmodel : PetViewModel = hiltViewModel()
    petviewmodel.getPetInfo()
    val title = stringResource(id = R.string.pet_catalog)
    val inCart = remember { mutableStateOf(false) }
    val animationData = remember { mutableStateOf(PetState()) }
    Scaffold(
        bottomBar = {
            AddToCartAnimation(inCart = inCart, data = animationData)
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                when (val response = cartViewModel.getCartData.value) {
                    is Response.Loading -> {
                        Log.d("CART", "Loading")
                    }

                    is Response.Error -> {
                        Log.d("CART", response.message)
                    }

                    is Response.Success -> {
                        Log.d("CART", response.data.toString())

                        when {
                            openBuySuccess.value -> {
                                SuccessDialog(
                                    onDismiss = {
                                        response.data?.forEach { data ->
                                            data?.items?.forEach { (_, value) ->
                                                if (value.quantity?.toInt() != 0) {
                                                    val cartData = CartState(
                                                        items = mutableMapOf(
                                                            value.title to CartItem(
                                                                description = value.description,
                                                                image = value.image,
                                                                price = value.price,
                                                                quantity = 0,
                                                                title = value.title
                                                            )
                                                        )
                                                    )
                                                    cartViewModel.setCartInfo(userData, cartData)
                                                }
                                            }
                                        }
                                        openBuySuccess.value = false
                                    },
                                    title = "Compra exitosa",
                                    message = "Gracias por su compra",
                                    products = response.data,
                                    icon = Icons.Filled.Check,
                                )
                            }

                        }
                        if (cartViewModel.showBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            cartViewModel.showBottomSheet = false
                                        }
                                    }
                                },
                                sheetState = sheetState
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    text = "Carrito",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                // Sheet content
                                response.data?.forEach { data ->
                                    if (data != null) {
                                        CartItem(userData, data.items, cartViewModel, openBuySuccess)
                                    }
                                }
                            }
                        }
                    }
                }
                when(val response = petviewmodel.getPets.value) {
                    is Response.Loading -> {
                        Text(text = "Cargando...")
                    }
                    is Response.Error -> {
                        Text(text = response.message)
                    }
                    is Response.Success -> {
                        Log.d("PetCatalog", "PetCatalog ID: ${response.data}")
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            modifier = Modifier.padding(5.dp)
                        ) {
                            response.data?.forEach { pet ->
                                item {
                                    if (pet != null) {
                                        Cards(
                                            userData = userData,
                                            pet,
                                            cartViewModel = cartViewModel,
                                            inCart = inCart,
                                            animationData = animationData,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
