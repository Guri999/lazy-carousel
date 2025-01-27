package kr.co.carousel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import kr.co.scope.LazyCarouselScope

@Composable
fun CenterCarousel(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: LazyCarouselScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val screenWidthPx = with(LocalDensity.current) { remember { screenWidth.toPx() } }
        val carouselState = rememberCarouselState(
            screenWidth = screenWidth,
        )

        LaunchedEffect(carouselState.itemsSize) {
            carouselState.setInfinityScrollState(carouselState.itemsSize)
            carouselState.animateToCenterItem(screenWidthPx)
        }
        LaunchedEffect(carouselState.isAnimationEnd) {
            carouselState.animateToCenterItem(screenWidthPx)
        }

        CarouselLazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            lazyCarouselState = carouselState,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement
        ) {
            content()
        }
    }
}