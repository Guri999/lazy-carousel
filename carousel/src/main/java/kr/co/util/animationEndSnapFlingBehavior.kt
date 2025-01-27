package kr.co.util

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal fun animationEndSnapFlingBehavior(
    lazyListState: LazyListState,
    isAnimationEnd: Boolean,
): FlingBehavior {
    val snapShotProvider = remember(isAnimationEnd) {
        if (isAnimationEnd) {
            SnapLayoutInfoProvider(lazyListState)
        } else null
    }

    return if (isAnimationEnd && snapShotProvider != null) {
        rememberSnapFlingBehavior(snapLayoutInfoProvider = snapShotProvider)
    } else {
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                return initialVelocity
            }
        }
    }
}