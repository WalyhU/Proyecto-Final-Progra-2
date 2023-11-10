package gt.edu.miumg.petstore.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.viewmodels.FavoriteViewModel

@Composable
fun FavoriteItem(
    data: PetState,
    userData: UserData,
    modifier: Modifier,
    favoriteViewModel: FavoriteViewModel
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            AsyncImage(
                model = data.image!!,
                contentDescription = "Pet Image",
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder_image),
            )
        },
        headlineContent = { Text(data.title!!) },
        trailingContent = {
            IconButton(
                onClick = {
                    favoriteViewModel.deleteFavoriteInfo(userData, data)
                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.unfavorite),
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        )
}