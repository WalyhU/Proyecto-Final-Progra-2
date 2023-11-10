package gt.edu.miumg.petstore.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.components.AddToCartAnimation
import gt.edu.miumg.petstore.components.Cards
import gt.edu.miumg.petstore.components.CartItem
import gt.edu.miumg.petstore.components.FloatingCarritoButton
import gt.edu.miumg.petstore.components.PetDetails
import gt.edu.miumg.petstore.components.SuccessDialog
import gt.edu.miumg.petstore.models.CartItem
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import gt.edu.miumg.petstore.viewmodels.CartViewModel
import gt.edu.miumg.petstore.viewmodels.FavoriteViewModel
import gt.edu.miumg.petstore.viewmodels.PetViewModel
import gt.edu.miumg.petstore.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessoriesCatalog(
    userData: UserData,
    navController: NavController,
) {
    val title = stringResource(id = R.string.accessories_catalog)
    // Carrito
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val cartViewModel: CartViewModel = hiltViewModel()
    val inCart = remember { mutableStateOf(false) }
    cartViewModel.getCartInfo(userData)
    // Comprar
    val openBuySuccess = remember { mutableStateOf(false) }
    // Datos
    val dataPet = remember { mutableStateOf(PetState()) }
    val petviewmodel: PetViewModel = hiltViewModel()
    petviewmodel.getAccessoryInfo()
    // Detalles
    val openDetails = remember { mutableStateOf(false) }
    // Favoritos
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()
    val inFavorites = remember { mutableStateOf(false) }
    // Busqueda
    val searchViewModel: SearchViewModel = hiltViewModel()
    val query = remember { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }
    searchViewModel.getAllSearch(query.value)

    Scaffold(
        bottomBar = {
            AddToCartAnimation(inCart = inCart, data = dataPet)
        },
        floatingActionButton = {
            FloatingCarritoButton(
                onClick = {
                    scope.launch { sheetState.show() }
                    cartViewModel.showBottomSheet = true
                })
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                SearchBar(
                    query = query.value,
                    onQueryChange = { query.value = it },
                    onSearch = { searchViewModel.getAllSearch(query.value) },
                    active = activeSearch.value,
                    onActiveChange = { activeSearch.value = it },
                    placeholder = { if (!activeSearch.value) Text("Buscar...") else Text("Buscar en el catálogo de accesorios") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    tonalElevation = 1.dp,
                    shadowElevation = 5.dp,
                    trailingIcon = {
                        if (activeSearch.value) {
                            IconButton(onClick = {
                                activeSearch.value = false
                                query.value = ""
                            }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Cancel Icon",
                                )
                            }
                        } else {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Search Icon",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    content = {
                        when (val response = searchViewModel.getAllSearch.value) {
                            is Response.Loading -> {
                                Text(text = "Cargando...")
                            }

                            is Response.Error -> {
                                Text(text = response.message)
                            }

                            is Response.Success -> {
                                Log.d("SEARCH", "Success: ${response.data.toString()}")
                                response.data?.forEach { data ->
                                    if (data != null) {
                                        data?.accessories?.forEach { pet ->
                                            ListItem(
                                                // Agregar imagen de la mascota
                                                leadingContent = {
                                                    AsyncImage(
                                                        model = pet.image,
                                                        contentDescription = "Pet Image",
                                                        alignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .size(45.dp)
                                                            .clip(CircleShape),
                                                        contentScale = ContentScale.Crop,
                                                        placeholder = painterResource(id = R.drawable.placeholder_image),
                                                    )
                                                },
                                                headlineContent = { Text(pet.title.toString()) },
                                                supportingContent = { Text(pet.price.toString()) },
                                                trailingContent = {
                                                    Row {
                                                        Button(
                                                            onClick = {
                                                                dataPet.value = pet
                                                                openDetails.value = true
                                                            },
                                                            modifier = Modifier
                                                                .width(120.dp),
                                                        ) {
                                                            Icon(
                                                                Icons.Filled.Add,
                                                                contentDescription = "Add Icon",
                                                                modifier = Modifier.size(15.dp)
                                                            )
                                                            Text(
                                                                text = " Detalles",
                                                                style = MaterialTheme.typography.bodySmall
                                                            )
                                                        }
                                                    }
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
                when {
                    openDetails.value -> {
                        PetDetails(
                            onDismiss = {
                                openDetails.value = false
                            },
                            data = dataPet.value,
                            inCart = inCart,
                            cartViewModel = cartViewModel,
                            userData = userData,
                            favoriteViewModel = favoriteViewModel,
                            inFavorites = inFavorites,
                        )
                    }
                }
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
                                LazyColumn(
                                    content = {
                                        response.data?.let { it1 ->
                                            items(it1.size) {
                                                response.data?.forEach { data ->
                                                    if (data != null) {
                                                        CartItem(
                                                            userData,
                                                            data.items,
                                                            cartViewModel,
                                                            openBuySuccess
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                when (val response = petviewmodel.getAccessory.value) {
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
                            modifier = Modifier.padding(top = 5.dp)
                        ) {
                            response.data?.forEach { food ->
                                item {
                                    if (food != null) {
                                        Cards(
                                            userData = userData,
                                            food,
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .clickable {
                                                    openDetails.value = true
                                                    dataPet.value = food
                                                },
                                            cartViewModel = cartViewModel,
                                            favoriteViewModel = favoriteViewModel,
                                            inCart = inCart,
                                            animationData = dataPet,
                                            inFavorites = inFavorites
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
