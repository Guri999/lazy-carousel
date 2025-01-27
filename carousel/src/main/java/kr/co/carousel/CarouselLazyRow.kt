package kr.co.carousel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kr.co.scope.LazyCarouselScope
import kr.co.scope.createCarouselScope
import kr.co.util.animationEndSnapFlingBehavior

@Composable
internal fun CarouselLazyRow(
    lazyCarouselState: LazyCarouselState,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: LazyCarouselScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = lazyCarouselState.listState,
        flingBehavior = animationEndSnapFlingBehavior(lazyCarouselState.listState, lazyCarouselState.isAnimationEnd.value),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
    ) {
        createCarouselScope(lazyCarouselState, this).apply(content)
    }
}
