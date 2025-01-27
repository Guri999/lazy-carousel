package kr.co.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
internal fun Dp.toPx(): Float = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
internal fun Float.toDp(): Dp = with(LocalDensity.current) { this@toDp.toDp() }