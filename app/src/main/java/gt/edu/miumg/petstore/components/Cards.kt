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
import gt.edu.miumg.petstore.models.CarritoState
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.viewmodels.CarritoViewModel

@Composable
fun Cards(
    data: PetState,
    modifier: Modifier = Modifier.padding(5.dp),
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    carritoViewModel: CarritoViewModel
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
            Column (
                modifier = Modifier
                    .width(120.dp),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = data.description as String,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
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
            Column (
                modifier = Modifier
                    .height(90.dp)
                    .width(50.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = colorResource(id = R.color.favorite_color),
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                )
                IconButton(onClick = {
                    val cartData = CarritoState(
                        uid = "123" + data.title,
                        title = data.title,
                        description = data.description,
                        price = data.price,
                        image = data.image,
                        quantity = 1
                    )
                    carritoViewModel.addCarrito(cartData)
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