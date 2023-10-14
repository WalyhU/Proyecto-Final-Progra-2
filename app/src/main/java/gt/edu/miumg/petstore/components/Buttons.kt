package gt.edu.miumg.petstore.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TonalButton(onClick: () -> Unit, text: String) {
    FilledTonalButton(
        onClick = { onClick },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text)
    }
}