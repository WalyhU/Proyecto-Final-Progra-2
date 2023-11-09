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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import gt.edu.miumg.petstore.models.PetState
import gt.edu.miumg.petstore.sign_in.UserData
import gt.edu.miumg.petstore.util.Response
import gt.edu.miumg.petstore.viewmodels.CartViewModel
import gt.edu.miumg.petstore.viewmodels.PetViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselCards(
    userData: UserData,
    inCart: MutableState<Boolean>,
    animationImage: MutableState<PetState>,
) {
    // Test para extraer datos de firebase
    val petviewmodel : PetViewModel = hiltViewModel()
    petviewmodel.getPetInfo()
    val cartViewModel: CartViewModel = hiltViewModel()
    when(val response = petviewmodel.getPets.value) {
        is Response.Success -> {
            val pets = response.data
            if (pets != null) {
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
                        pets[page]?.let {
                            Cards(
                                animationData = animationImage,
                                inCart = inCart,
                                userData = userData,
                                data = it,
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
                                shape = RoundedCornerShape(35.dp),
                                cartViewModel = cartViewModel
                            )
                        }
                    }
                }
            }
        }
        is Response.Error -> {
            Text(text = response.message)
        }
        is Response.Loading -> {
            Text(text = "Loading...")
        }
    }
}