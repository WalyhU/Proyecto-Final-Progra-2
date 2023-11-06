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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.components.Cards
import gt.edu.miumg.petstore.util.Response
import gt.edu.miumg.petstore.viewmodels.CarritoViewModel
import gt.edu.miumg.petstore.viewmodels.PetViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCatalog(navController: NavController) {
    // Test para extraer datos de firebase
    val petviewmodel : PetViewModel = hiltViewModel()
    petviewmodel.getPetInfo()
    val title = stringResource(id = R.string.pet_catalog)
    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                when(val response = petviewmodel.getPets.value) {
                    is Response.Loading -> {
                        Text(text = "Cargando...")
                    }
                    is Response.Error -> {
                        Text(text = response.message)
                    }
                    is Response.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            modifier = Modifier.padding(5.dp)
                        ) {
                            response.data?.forEach { pet ->
                                item {
                                    Cards(
                                        pet,
                                        carritoViewModel = CarritoViewModel()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}