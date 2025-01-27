package kr.co.carousel

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.onStart
import kotlin.math.abs

@Stable
class LazyCarouselState(
    private val screenWidth: Dp,
    val listState: LazyListState,
) {
    private val zoomItemSize = mutableStateOf(0.dp)
    val extraSpace: Dp get() = (screenWidth - zoomItemSize.value) / 2

    var isAnimationEnd = mutableStateOf(true)
        private set

    var itemsSize: Int = 0
        private set

    private var scrollType: CarouselScrollType = CarouselScrollType.DEFAULT

    private var currentItemIndex: Int = 0

    private var isInitialized = false

    fun initialize(
        zoomSize: Dp,
        size: Int,
        type: CarouselScrollType
    ) {
        if (!isInitialized) {
            zoomItemSize.value = zoomSize
            itemsSize = size
            scrollType = type
            isInitialized = true
        }
    }

    fun updateCurrentItemIndex(index: Int = currentItemIndex) {
        currentItemIndex = index
    }

    fun setAnimationEnd(isEnd: Boolean) {
        isAnimationEnd.value = isEnd
    }

    suspend fun animateToCenterItem(screenWidth: Float) {
        snapshotFlow { listState.isScrollInProgress }
            .onStart { emit(false) }
            .collect { isScrolling ->
                if (isScrolling.not() && isAnimationEnd.value) {
                    val scrollOffset = calculateCenterOffset(screenWidth)
                    if (abs(scrollOffset) > 1.dp.value) {
                        listState.animateScrollBy(scrollOffset)
                    }
                }
            }
    }

    internal suspend fun setInfinityScrollState(itemsSize: Int) {
        if (scrollType == CarouselScrollType.INFINITE) {
            val totalCount = Int.MAX_VALUE / 10000
            val centerIndex = totalCount / 2f
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val startIndex = centerIndex - (centerIndex % itemsSize) - (visibleItems.size / 2)
            listState.scrollToItem(startIndex.toInt())
        }
    }

    private fun calculateCenterOffset(width: Float): Float {
        val center = width / 2
        val visibleItems = listState.layoutInfo.visibleItemsInfo
        val centerItem = visibleItems.minByOrNull {
            abs((it.offset + it.size / 2) - center)
        }
        return centerItem?.let { it.offset + it.size / 2 - center } ?: 0f
    }
}

enum class CarouselScrollType {
    DEFAULT,
    INFINITE,
}

@Composable
internal fun rememberCarouselState(
    screenWidth: Dp,
    listState: LazyListState = rememberLazyListState(),
) = remember {
    LazyCarouselState(screenWidth, listState)
}