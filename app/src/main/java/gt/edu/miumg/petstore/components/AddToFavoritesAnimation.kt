package gt.edu.miumg.petstore.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.models.PetState

@OptIn(ExperimentalMotionApi::class)
@Composable
fun AddToFavoritesAnimation(
    inFavorites: MutableState<Boolean>,
    data: MutableState<PetState>,
) {
    var density = LocalDensity.current
    // Un composable que muestra el producto con una animación de entrada
    // Slide in vertically + expand vertically + fade in
    AnimatedVisibility(
        visible = inFavorites.value,
        // Hacer que entre desde abajo con un Slide in vertically + expand vertically + fade in
        enter = slideInVertically(initialOffsetY = { with(density) { -40.dp.roundToPx() } }) +
                expandVertically(expandFrom = Alignment.Top) +
                fadeIn(initialAlpha = 0.3f),
        // Que la animacion desaparezca luego de dos segundos
        exit = slideOutVertically(tween(2000)) + shrinkVertically(tween(2000)) + fadeOut(tween(2000)),
    ) {
        ListItem(
            // Agregar imagen de la mascota
            leadingContent = {
                AsyncImage(
                    model = data.value.image,
                    contentDescription = "Pet Image",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder_image),
                )
            },
            headlineContent = { Text(data.value.title.toString()) },
            supportingContent = { Text(data.value.price.toString()) },
            trailingContent = {
                Row {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorite Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(text = "Agregado a favoritos", style = MaterialTheme.typography.titleMedium)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            // Elevalo un poco con tonalElevation y shadowElevation
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
        )
    }
    // Esperar dos segundos antes de pasar a falso
    if (inFavorites.value) {
        android.os.Handler().postDelayed({
            inFavorites.value = false
        }, 2000)
    }
}
