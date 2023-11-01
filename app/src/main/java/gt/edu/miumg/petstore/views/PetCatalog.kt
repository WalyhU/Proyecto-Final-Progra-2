package gt.edu.miumg.petstore.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.components.Cards

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCatalog(navController: NavController) {
    val title = stringResource(id = R.string.pet_catalog)
    val pets = mapOf(
        "Dog" to mapOf(
            "image" to R.drawable.perro,
            "title" to "Dog",
            "description" to "Dog description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        "Cat" to mapOf(
            "image" to R.drawable.gato,
            "title" to "Cat",
            "description" to "Cat description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        "Bird" to mapOf(
            "image" to R.drawable.pajaro,
            "title" to "Bird",
            "description" to "Bird description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        "Fish" to mapOf(
            "image" to R.drawable.pez,
            "title" to "Fish",
            "description" to "Fish description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        "Hamster" to mapOf(
            "image" to R.drawable.hamster,
            "title" to "Hamster",
            "description" to "Hamster description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        )
    )
    Scaffold (
        content = {
            Column (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    pets.forEach { (key, value) ->
                        item {
                            Cards(
                                data = value,
                            )
                        }
                    }
                }
            }
        }
    )
}