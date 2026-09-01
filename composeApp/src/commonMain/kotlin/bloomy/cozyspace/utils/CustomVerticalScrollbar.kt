package bloomy.cozyspace.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import kotlin.math.roundToInt

@Suppress("FrequentlyChangingValue")
@Composable
fun CustomVerticalScrollbar(
    listState: LazyListState,
    itemCount: Int,
    modifier: Modifier = Modifier,
    trackWidth: Dp = 14.dp,
    thumbWidth: Dp = 38.dp,
    trackColor: Color = LightGreen,
    thumbColor: Color = DarkGreen,
    gripColor: Color = WhiteBackground
) {
    if (itemCount == 0) return

    BoxWithConstraints(
        modifier = modifier.width(thumbWidth) // le conteneur doit être assez large pour laisser le curseur déborder de la piste
    ) {
        val density = LocalDensity.current
        val trackHeightPx = with(density) { maxHeight.toPx() }

        val visibleCount = listState.layoutInfo.visibleItemsInfo.size.coerceAtLeast(1)
        val thumbRatio = (visibleCount.toFloat() / itemCount).coerceIn(0.15f, 1f)
        val minThumbHeightPx = with(density) { 56.dp.toPx() }
        val thumbHeightPx = (trackHeightPx * thumbRatio)
            .coerceAtLeast(minThumbHeightPx)
            .coerceAtMost(trackHeightPx)

        val maxScrollIndex = (itemCount - visibleCount).coerceAtLeast(1)
        val scrollProgress = (listState.firstVisibleItemIndex.toFloat() / maxScrollIndex).coerceIn(0f, 1f)
        val thumbOffsetPx = (trackHeightPx - thumbHeightPx) * scrollProgress

        // Piste : pilule fine centrée
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .width(trackWidth)
                .clip(RoundedCornerShape(50))
                .background(trackColor)
        )

        // Curseur : pilule large qui déborde, avec poignée
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, thumbOffsetPx.roundToInt()) }
                .width(thumbWidth)
                .height(with(density) { thumbHeightPx.toDp() })
                .clip(RoundedCornerShape(24.dp))
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(1.5.dp)
                            .background(gripColor)
                    )
                }
            }
        }
    }
}
