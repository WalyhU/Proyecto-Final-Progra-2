package gt.edu.miumg.petstore.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.components.AddToCartAnimation
import gt.edu.miumg.petstore.components.AddToFavoritesAnimation
import gt.edu.miumg.petstore.components.FavoriteItem
import gt.edu.miumg.petstore.components.PetDetails
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import gt.edu.miumg.petstore.viewmodels.CartViewModel
import gt.edu.miumg.petstore.viewmodels.FavoriteViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfilePage(
    userData: UserData?,
    onSignOut: () -> Unit,
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    if (userData != null) {
        cartViewModel.getCartInfo(userData)
    }
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()
    if (userData != null) {
        favoriteViewModel.getFavoritesInfo(userData)
    }
    // Detalles
    val openDetails = remember { mutableStateOf(false) }
    val dataPet = remember { mutableStateOf(PetState()) }
    // Animacion
    val inFavorites = remember { mutableStateOf(false) }
    val inCart = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    if (userData?.profilePictureUrl != null) {
                        AsyncImage(
                            model = userData.profilePictureUrl,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (userData?.username != null) {
                        Text(
                            text = userData.username,
                            textAlign = TextAlign.Left,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .width(200.dp)
                        )
                    }
                }
                Button(onClick = onSignOut) {
                    Text(text = "Cerrar Sesion", fontSize = 13.sp)
                }
            }
        },
        bottomBar = {
            AddToFavoritesAnimation(inFavorites = inFavorites, data = dataPet)
            AddToCartAnimation(inCart = inCart, data = dataPet)
        },
        modifier = Modifier
            .fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                content = {
                    when (val response = favoriteViewModel.getFavoritesData.value) {
                        is Response.Loading -> {
                            Text(
                                text = "Cargando...",
                                textAlign = TextAlign.Left,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp)
                            )
                        }
                        is Response.Error -> {
                            Text(
                                text = "Error",
                                textAlign = TextAlign.Left,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp)
                            )
                        }
                        is Response.Success -> {
                            Log.d("ProfilePage", "onCreate: ${response.data}")
                            when {
                                openDetails.value -> {
                                    if (userData != null) {
                                        PetDetails(
                                            onDismiss = {
                                                openDetails.value = false
                                            },
                                            data = dataPet.value,
                                            inCart = inCart,
                                            inFavorites = inFavorites,
                                            cartViewModel = cartViewModel,
                                            favoriteViewModel = favoriteViewModel,
                                            userData = userData,
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "FAVORITOS",
                                textAlign = TextAlign.Left,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp)
                            )

                            // Comprobar si el primer dato de la lista tiene el title "item" y si es el unico dato de la lista o si la lista esta vacia
                            if (response.data?.get(0)?.items?.get("0")?.title == "item"
                                && response.data?.get(0)?.items?.size == 1
                                || response.data?.get(0)?.items?.size == 0) {
                                Text(
                                    text = "No hay favoritos...",
                                    textAlign = TextAlign.Left,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                )
                            }


                            response.data?.forEach {
                                if (it != null) {
                                    it.items.forEach { (key, value) ->
                                        if (userData != null) {
                                            if (value.title != "item") {
                                                FavoriteItem(
                                                    data = value,
                                                    userData = userData,
                                                    favoriteViewModel = favoriteViewModel,
                                                    modifier = Modifier
                                                        .clickable {
                                                            openDetails.value = true
                                                            dataPet.value = value
                                                        }
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
    )
}
