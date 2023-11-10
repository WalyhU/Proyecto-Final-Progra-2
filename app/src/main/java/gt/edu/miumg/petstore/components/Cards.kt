package gt.edu.miumg.petstore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.models.CartState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.viewmodels.CartViewModel

@Composable
fun Cards(
    userData: UserData,
    data: PetState,
    modifier: Modifier = Modifier.padding(5.dp),
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    cartViewModel: CartViewModel,
    inCart: MutableState<Boolean>,
    animationData: MutableState<PetState>,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier,
        shape = shape
    ) {
        // El AsyncImage tiene que tener un tamaño de 100dp x 100dp y un padding de 16dp y los bordes redondeados de 8dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f) // Ajusta el valor según tu preferencia para la relación de aspecto
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.image)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.error_image),
                contentScale = ContentScale.Crop,
            )
        }
        // Crear un Column dentro del Card, el cual tendrá un Text con el título, un Text con la descripción y un Text con el precio.
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .width(120.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = data.title as String,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Limitar la descripcion a 2 lineas
                Text(
                    text = if (data.description.toString().length > 25) data.description.toString().substring(0, 25) + "..." else data.description.toString(),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Q${data.price}",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .height(90.dp)
                    .width(50.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = colorResource(id = R.color.favorite_color),
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                }
                IconButton(onClick = {
                    // Activar la animacion asignandole a initProgress el valor de progress
                    inCart.value = true
                    animationData.value = data
                    val cartData = CartState(
                        items = mutableMapOf(
                            data.title to gt.edu.miumg.petstore.models.CartItem(
                                description = data.description,
                                image = data.image,
                                price = data.price,
                                quantity = 1,
                                title = data.title
                            )
                        )
                    )
                    cartViewModel.setCartInfo(userData, cartData)
                }) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                }
            }
        }
    }


}
