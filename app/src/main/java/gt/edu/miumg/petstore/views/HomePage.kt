package gt.edu.miumg.petstore.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gt.edu.miumg.petstore.components.CarouselCards
import gt.edu.miumg.petstore.components.FloatingCarritoButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    // val title = stringResource(id = R.string.home)
    Scaffold(
        floatingActionButton = { FloatingCarritoButton(onClick = { /*TODO*/ }) },
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Inicio",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                CarouselCards()
            }
        }
    )
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage()
}