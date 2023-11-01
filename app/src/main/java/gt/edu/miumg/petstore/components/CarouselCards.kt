package gt.edu.miumg.petstore.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import gt.edu.miumg.petstore.R
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselCards() {
    val pets = mapOf(
        0 to mapOf(
            "image" to R.drawable.perro,
            "title" to "Dog",
            "description" to "Dog description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        1 to mapOf(
            "image" to R.drawable.gato,
            "title" to "Cat",
            "description" to "Cat description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        2 to mapOf(
            "image" to R.drawable.pajaro,
            "title" to "Bird",
            "description" to "Bird description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        3 to mapOf(
            "image" to R.drawable.pez,
            "title" to "Fish",
            "description" to "Fish description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        ),
        4 to mapOf(
            "image" to R.drawable.hamster,
            "title" to "Hamster",
            "description" to "Hamster description",
            "price" to 100.00,
            "buttonText" to "Buy",
            "onFavoriteClick" to { /*TODO*/ }
        )
    )
    val pagerState = rememberPagerState(pageCount = { pets.size })
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 65.dp),
            modifier = Modifier
                .height(350.dp),
        ) { page ->
            Cards(
                data = pets[page]?.toMutableMap() ?: mutableMapOf(),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        // We animate the alpha, between 50% and 100%

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                            .also {
                                // We animate the scaleX + scaleY, between 85% and 100%
                                scaleX = lerp(
                                    start = 0.85f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                                scaleY = lerp(
                                    start = 0.85f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                    }
                    .padding(10.dp),
                shape = RoundedCornerShape(35.dp)
            )
        }
    }
}