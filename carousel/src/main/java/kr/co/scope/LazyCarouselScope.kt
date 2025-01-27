package kr.co.scope

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import kr.co.carousel.CarouselScrollType
import kr.co.carousel.LazyCarouselState
import kotlin.math.abs

interface LazyCarouselScope : LazyListScope {
    val lazyCarouselState: LazyCarouselState
}

internal fun createCarouselScope(
    lazyCarouselState: LazyCarouselState,
    lazyListScope: LazyListScope
): LazyCarouselScope {
    return object : LazyCarouselScope, LazyListScope by lazyListScope {
        override val lazyCarouselState: LazyCarouselState = lazyCarouselState
    }
}

fun <T> LazyCarouselScope.zoomedCarouselItems(
    items: List<T>,
    itemSize: Dp,
    zoomItemSize: Dp,
    unfocusedAlpha: Float = 0.5f,
    scrollType: CarouselScrollType = CarouselScrollType.DEFAULT,
    itemContent: @Composable (item: T, zoomModifier: Modifier, isFocus: Boolean) -> Unit
) {

    lazyCarouselState.initialize(zoomItemSize, items.size, scrollType)

    carouselItems(items, scrollType) { item, isCurrentPage ->
        val targetSize = remember(isCurrentPage) {
            if (isCurrentPage) zoomItemSize else itemSize
        }
        val targetAlpha = remember(isCurrentPage) {
            if (isCurrentPage) 1f else unfocusedAlpha
        }
        val animatedSize by animateDpAsState(
            targetValue = targetSize,
            label = "animatedSize",
            finishedListener = { this@zoomedCarouselItems.lazyCarouselState.setAnimationEnd(true) }
        )
        val animatedAlpha by animateFloatAsState(targetAlpha, label = "animatedAlpha")
        itemContent(
            item,
            Modifier
                .size(animatedSize)
                .alpha(animatedAlpha),
            isCurrentPage
        )
    }
}


internal fun <T> LazyCarouselScope.carouselItems(
    items: List<T>,
    scrollType: CarouselScrollType = CarouselScrollType.DEFAULT,
    itemContent: @Composable LazyItemScope.(item: T, isCurrentPage: Boolean) -> Unit
) {
    when (scrollType) {
        CarouselScrollType.DEFAULT -> defaultCarouselItemsImpl(items, itemContent)
        CarouselScrollType.INFINITE -> infiniteCarouselItemsImpl(items, itemContent)
    }
}

private inline fun <T> LazyCarouselScope.defaultCarouselItemsImpl(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(item: T, isCurrentPage: Boolean) -> Unit
) {
    items(count = items.size + 2, key = { it }) { index ->
        val layoutInfo by remember { derivedStateOf { this@defaultCarouselItemsImpl.lazyCarouselState.listState.layoutInfo } }
        val visibleItems = layoutInfo.visibleItemsInfo
        val center = layoutInfo.viewportEndOffset / 2
        val centerItem = visibleItems.minByOrNull {
            abs((it.offset + it.size / 2) - center)
        }

        val isCurrentPage = index == centerItem?.index
        if (isCurrentPage) this@defaultCarouselItemsImpl.lazyCarouselState.updateCurrentItemIndex(
            index
        )

        val itemIndex = index - 1
        val item = if (itemIndex in items.indices) items[itemIndex] else null

        item?.let {
            itemContent(it, isCurrentPage)
        } ?: Spacer(
            modifier = Modifier.width(this@defaultCarouselItemsImpl.lazyCarouselState.extraSpace)
        )
    }
}

private inline fun <T> LazyCarouselScope.infiniteCarouselItemsImpl(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(item: T, isCurrentPage: Boolean) -> Unit
) {
    items(count = Int.MAX_VALUE / 10000, key = { it }) { index ->
        val layoutInfo by remember { derivedStateOf { this@infiniteCarouselItemsImpl.lazyCarouselState.listState.layoutInfo } }
        val visibleItems = layoutInfo.visibleItemsInfo
        val center = layoutInfo.viewportEndOffset / 2
        val centerItem = visibleItems.minByOrNull {
            abs((it.offset + it.size / 2) - center)
        }

        val isCurrentPage = index == centerItem?.index
        if (isCurrentPage) this@infiniteCarouselItemsImpl.lazyCarouselState.updateCurrentItemIndex(
            index
        )

        val item = items[index % items.size]
        itemContent(item, isCurrentPage)
    }
}