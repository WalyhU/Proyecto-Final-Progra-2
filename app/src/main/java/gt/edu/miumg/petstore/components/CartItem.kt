package gt.edu.miumg.petstore.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import gt.edu.miumg.petstore.models.CarritoState
import gt.edu.miumg.petstore.viewmodels.CarritoViewModel

@Composable
fun CartItem(
    data: CarritoState,
    carritoViewModel: CarritoViewModel
) {
    ListItem(
        headlineContent = { Text(data.title) },
        supportingContent = { Text(data.price.toString()) },
        // Boton para eliminar un item del carrito
        trailingContent = {
            IconButton(onClick = { carritoViewModel.removeCarrito(data) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}