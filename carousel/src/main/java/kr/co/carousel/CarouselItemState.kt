package kr.co.carousel

import androidx.compose.ui.Modifier

data class CarouselItemState<T>(
    val item: T,
    val animatedModifier: Modifier,
    val isFocus: Boolean,
)
