package gt.edu.miumg.petstore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.models.CartState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    title: String,
    message: String,
    icon: ImageVector,
    products: MutableList<CartState?>?
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Success",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    // Mostrar listasdo de los productos
                    if (products != null) {
                        products.forEach { sellproduct ->
                            sellproduct?.items?.forEach { (key, value) ->
                                if (value.quantity?.toInt() != 0) {
                                    ListItem(
                                        colors = ListItemDefaults.colors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                                                Text(
                                                    value.quantity.toString(),
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Mostrar total de la compra
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total: Q${
                                // Convertir la cantidad a Double
                                products?.get(0)?.items?.map { it.value.price?.times(it.value.quantity ?: 0) }
                                    ?.sumByDouble { it ?: 0.0 }
                            }",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        },
    )
}