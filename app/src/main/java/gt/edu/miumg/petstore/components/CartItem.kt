package gt.edu.miumg.petstore.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.models.CartItem
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.viewmodels.CartViewModel
import kotlin.math.absoluteValue

@Composable
fun CartItem(
    userData: UserData,
    data: MutableMap<String?, CartItem>,
    cartViewModel: CartViewModel,
    openBuySuccess: MutableState<Boolean>
) {
    // Si todos los documentos tienen una cantidad de 0, se muestra una imagen de carrito vacio
    if (data.all { (_, value) -> value.quantity?.toInt() == 0 }) {
        AsyncImage(
            model = "https://www.distritomoda.com.ar/sites/all/themes/omega_btob/images/carrito_vacio_nuevo.png",
            contentDescription = "Empty Cart",
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = painterResource(id = R.drawable.placeholder_image),
        )
    }
    data?.forEach { (key, value) ->
        AnimatedVisibility(value.quantity?.toInt() != 0) {
            ListItem(
                modifier = Modifier
                    .scrollable(
                        state = rememberScrollableState { delta ->
                            // Disallow scrolling if the user is trying to scroll horizontally
                            // This is to allow the parent LazyColumn to scroll vertically
                            if (delta.absoluteValue > 0f) {
                                0f
                            } else {
                                delta
                            }
                        },
                        orientation = Orientation.Vertical,
                        enabled = true
                    ),
                // Agregar imagen de la mascota
                leadingContent = {
                    AsyncImage(
                        model = value.image.toString(),
                        contentDescription = "Pet Image",
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.placeholder_image),
                    )
                },
                headlineContent = { Text(value.title.toString()) },
                supportingContent = { Text(value.price.toString()) },
                // Boton para eliminar un item del carrito
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            enabled = value.quantity?.toInt() != 1,
                            onClick = {
                                val cartData = CartState(
                                    items = mutableMapOf(
                                        value.title to CartItem(
                                            description = value.description,
                                            image = value.image,
                                            price = value.price,
                                            quantity = value.quantity?.minus(1),
                                            title = value.title
                                        )
                                    )
                                )
                                cartViewModel.setCartInfo(userData, cartData)
                            }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                tint = if (value.quantity?.toInt() == 1) MaterialTheme.colorScheme.onSurface else (MaterialTheme.colorScheme.secondary),
                                contentDescription = "Minus item from cart"
                            )
                        }
                        Text(value.quantity.toString(), color = MaterialTheme.colorScheme.secondary)
                        IconButton(onClick = {
                            val cartData = CartState(
                                items = mutableMapOf(
                                    value.title to CartItem(
                                        description = value.description,
                                        image = value.image,
                                        price = value.price,
                                        quantity = value.quantity?.plus(1),
                                        title = value.title
                                    )
                                )
                            )
                            cartViewModel.setCartInfo(userData, cartData)
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "Add item to cart"
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                        ) {
                            Text(
                                text = "Subtotal: ",
                                fontSize = 8.5.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(text = "Q${value.price?.times(value.quantity ?: 0)}")
                        }

                        IconButton(
                            modifier = Modifier
                                .size(24.dp),
                            onClick = {
                                val cartData = CartState(
                                    items = mutableMapOf(
                                        value.title to CartItem(
                                            description = value.description,
                                            image = value.image,
                                            price = value.price,
                                            quantity = 0L,
                                            title = value.title
                                        )
                                    )
                                )
                                cartViewModel.setCartInfo(userData, cartData)
                            }) {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp),
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            )
        }
    }
    // Mostrar el total de la compra
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Total: Q${
                data?.map { it.value.price?.times(it.value.quantity ?: 0) }
                    ?.sumByDouble { it ?: 0.0 }?.let {
                        String.format("%.2f", it)
                    }
            }",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )

        // Boton de compra
        Button(
            enabled = data?.any { it.value.quantity?.toInt() != 0 } ?: false,
            modifier = Modifier
                .height(34.dp),
            onClick = {
                openBuySuccess.value = true
                cartViewModel.showBottomSheet = false
            }) {
            Icon(
                Icons.Outlined.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(text = "Comprar", modifier = Modifier.padding(start = 8.dp))
        }
    }
}
