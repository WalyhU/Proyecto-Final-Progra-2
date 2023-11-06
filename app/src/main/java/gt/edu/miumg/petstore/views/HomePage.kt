package gt.edu.miumg.petstore.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import gt.edu.miumg.petstore.R
import gt.edu.miumg.petstore.components.CarouselCards
import gt.edu.miumg.petstore.components.CartItem
import gt.edu.miumg.petstore.components.FloatingCarritoButton
import gt.edu.miumg.petstore.viewmodels.CarritoViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(carritoViewModel: CarritoViewModel) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingCarritoButton(
                onClick = {
                    scope.launch { sheetState.show() }
                    carritoViewModel.showBottomSheet = true
                })
        },
    ) { contentPadding ->
        // Screen content
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Inicio",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            CarouselCards(carritoViewModel)
        }

        if (carritoViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            carritoViewModel.showBottomSheet = false
                        }
                    }
                },
                sheetState = sheetState
            ) {
                // Sheet content
                if (carritoViewModel._carrito.size == 0) {
                    AsyncImage(
                        model = "https://www.distritomoda.com.ar/sites/all/themes/omega_btob/images/carrito_vacio_nuevo.png",
                        contentDescription = "Empty Cart",
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        placeholder = painterResource(id = R.drawable.placeholder_image),
                    )
                } else {
                    carritoViewModel.getCarrito().forEach { data ->
                        CartItem(data, carritoViewModel)
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage(CarritoViewModel())
}