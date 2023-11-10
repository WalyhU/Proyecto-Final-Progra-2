package gt.edu.miumg.petstore.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.components.AddToCartAnimation
import gt.edu.miumg.petstore.components.CarouselCards
import gt.edu.miumg.petstore.components.CartItem
import gt.edu.miumg.petstore.components.FloatingCarritoButton
import gt.edu.miumg.petstore.components.PetDetails
import gt.edu.miumg.petstore.components.SuccessDialog
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import gt.edu.miumg.petstore.viewmodels.CartViewModel
import gt.edu.miumg.petstore.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    userData: UserData,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val cartViewModel: CartViewModel = hiltViewModel()
    cartViewModel.getCartInfo(userData)
    val searchViewModel: SearchViewModel = hiltViewModel()
    val query = remember { mutableStateOf("") }
    val activeSearch = remember { mutableStateOf(false) }
    searchViewModel.getAllSearch(query.value)
    val openBuySuccess = remember { mutableStateOf(false) }
    val openDetails = remember { mutableStateOf(false) }
    val inCart = remember { mutableStateOf(false) }
    val dataPet = remember { mutableStateOf(PetState()) }
    Scaffold(
        floatingActionButton = {
            FloatingCarritoButton(
                onClick = {
                    scope.launch { sheetState.show() }
                    cartViewModel.showBottomSheet = true
                })
        },
        bottomBar = {
            AddToCartAnimation(inCart = inCart, data = dataPet)
        }
    ) { contentPadding ->
        // Screen content
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Inicio",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            SearchBar(
                query = query.value,
                onQueryChange = { query.value = it },
                onSearch = { searchViewModel.getAllSearch(query.value) },
                active = activeSearch.value,
                onActiveChange = { activeSearch.value = it },
                placeholder = { if (!activeSearch.value) Text("Buscar...") else Text("Buscar entre todo el catalogo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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
                            Log.d("SEARCH", "Cargando...")
                        }

                        is Response.Error -> {
                            Log.d("SEARCH", "Error: ${response.message}")
                        }

                        is Response.Success -> {
                            Log.d("SEARCH", "Success: ${response.data.toString()}")
                            response.data?.forEach { data ->
                                if (data != null) {
                                    data?.pets?.forEach {
                                        ListItem(
                                            // Agregar imagen de la mascota
                                            leadingContent = {
                                                AsyncImage(
                                                    model = it.image,
                                                    contentDescription = "Pet Image",
                                                    alignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(45.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop,
                                                    placeholder = painterResource(id = R.drawable.placeholder_image),
                                                )
                                            },
                                            headlineContent = { Text(it.title.toString()) },
                                            supportingContent = { Text("Categoria: Mascotas") },
                                            trailingContent = {
                                                Row {
                                                    Button(
                                                        onClick = {
                                                        dataPet.value = it
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
                                    data?.food?.forEach {
                                        ListItem(
                                            // Agregar imagen de la mascota
                                            leadingContent = {
                                                AsyncImage(
                                                    model = it.image,
                                                    contentDescription = "Food Image",
                                                    alignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(45.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop,
                                                    placeholder = painterResource(id = R.drawable.placeholder_image),
                                                )
                                            },
                                            headlineContent = { Text(it.title.toString()) },
                                            supportingContent = { Text("Categoria: Alimentos") },
                                            trailingContent = {
                                                Row {
                                                    Button(
                                                        onClick = {
                                                            dataPet.value = it
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
                                    data?.accessories?.forEach {
                                        ListItem(
                                            // Agregar imagen de la mascota
                                            leadingContent = {
                                                AsyncImage(
                                                    model = it.image,
                                                    contentDescription = "Accessory Image",
                                                    alignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(45.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop,
                                                    placeholder = painterResource(id = R.drawable.placeholder_image),
                                                )
                                            },
                                            headlineContent = { Text(it.title.toString()) },
                                            supportingContent = { Text("Categoria: Accesorios") },
                                            trailingContent = {
                                                Row {
                                                    Button(
                                                        onClick = {
                                                            dataPet.value = it
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
            CarouselCards(
                openDetails = openDetails,
                inCart = inCart,
                dataPet = dataPet,
                userData = userData,
            )
        }
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
                                                    value.title to gt.edu.miumg.petstore.models.CartItem(
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
    }
}