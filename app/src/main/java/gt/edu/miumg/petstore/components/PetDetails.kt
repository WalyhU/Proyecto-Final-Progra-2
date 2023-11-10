package gt.edu.miumg.petstore.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.models.FavoriteState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import gt.edu.miumg.petstore.viewmodels.CartViewModel
import gt.edu.miumg.petstore.viewmodels.FavoriteViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetails(
    onDismiss: () -> Unit,
    data: PetState,
    inCart: MutableState<Boolean>,
    cartViewModel: CartViewModel,
    userData: UserData,
    favoriteViewModel: FavoriteViewModel,
    inFavorites: MutableState<Boolean>
) {
    val selectQuantity = remember { mutableStateOf(1) }
    AlertDialog(
        onDismissRequest = onDismiss,
        content = {
            ElevatedCard(
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = data.image,
                        contentDescription = "Pet image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(320.dp)
                            .fillMaxWidth(),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = data.title.toString().toUpperCase(),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(203.dp)
                                .padding(8.dp)
                        )
                        Column (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End

                        ) {
                            Text(
                                text = "Precio:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "Q.${data.price}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Descripción:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = data.description.toString(),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Cantidad:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    IconButton(
                                        enabled = selectQuantity.value != 1,
                                        onClick = {
                                            selectQuantity.value--
                                        }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                            tint = if (selectQuantity.value == 1) MaterialTheme.colorScheme.onSurface else (MaterialTheme.colorScheme.secondary),
                                            contentDescription = "Minus item from cart"
                                        )
                                    }
                                    Text(selectQuantity.value.toString(), color = MaterialTheme.colorScheme.secondary)
                                    IconButton(onClick = {
                                        selectQuantity.value++
                                    }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            tint = MaterialTheme.colorScheme.secondary,
                                            contentDescription = "Add item to cart"
                                        )
                                    }
                                }
                            }

                            if (comprobarFavorito(userData, data, favoriteViewModel)) {
                                IconButton(onClick = {
                                    favoriteViewModel.deleteFavoriteInfo(userData, data)
                                }) {
                                    Icon(
                                        painterResource(id = R.drawable.unfavorite),
                                        contentDescription = null,
                                        tint = colorResource(id = R.color.favorite_color),
                                        modifier = Modifier
                                            .height(20.dp)
                                            .width(20.dp)
                                    )
                                }
                            } else {
                                IconButton(onClick = {
                                    inFavorites.value = true
                                    val favoriteData = FavoriteState(
                                        items = mutableMapOf(
                                            data.title to PetState(
                                                description = data.description,
                                                image = data.image,
                                                price = data.price,
                                                title = data.title
                                            )
                                        )
                                    )
                                    favoriteViewModel.setFavoriteInfo(userData, favoriteData)
                                }) {
                                    Icon(
                                        Icons.Outlined.FavoriteBorder,
                                        contentDescription = null,
                                        tint = colorResource(id = R.color.favorite_color),
                                        modifier = Modifier
                                            .height(20.dp)
                                            .width(20.dp)
                                    )
                                }
                            }
                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            onClick = {
                            inCart.value = true
                            val cartData = CartState(
                                items = mutableMapOf(
                                    data.title to gt.edu.miumg.petstore.models.CartItem(
                                        description = data.description,
                                        image = data.image,
                                        price = data.price,
                                        quantity = selectQuantity.value.toLong(),
                                        title = data.title
                                    )
                                )
                            )
                            cartViewModel.setCartInfo(userData, cartData)
                        }) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp)
                            )
                            Text(
                                text = "Agregar al carrito",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    )

    // Comprobar si el producto esta en favoritos
    fun comprobarFavorito(
        userData: UserData,
        data: PetState,
        favoriteViewModel: FavoriteViewModel
    ): Boolean {
        var inFavorites = false
        favoriteViewModel.getFavoritesInfo(userData)
        when (val response = favoriteViewModel.getFavoritesData.value) {
            is Response.Success -> {
                val favorites = response.data
                if (favorites != null) {
                    favorites.forEach { favorite ->
                        if (favorite != null) {
                            if (favorite.items.containsKey(data.title)) {
                                inFavorites = true
                            }
                        }
                    }
                }
            }
            else -> {
                inFavorites = false
            }
        }
        return inFavorites
    }

}