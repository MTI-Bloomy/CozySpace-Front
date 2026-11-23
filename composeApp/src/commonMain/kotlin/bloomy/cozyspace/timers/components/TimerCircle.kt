package bloomy.cozyspace.timers.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen

@Composable
fun TimerCircle(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val boundedProgress = progress.coerceIn(0f, 1f)

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val arcRadius = size.minDimension / 2f
        val lightCircleRadius = arcRadius - size.minDimension * 0.012f

        drawCircle(
            color = LightGreen,
            radius = lightCircleRadius,
            center = center,
        )
        when {
            boundedProgress >= 1f -> {
                drawCircle(
                    color = DarkGreen,
                    radius = arcRadius,
                    center = center,
                )
            }

            boundedProgress > 0f -> {
                drawArc(
                    color = DarkGreen,
                    startAngle = -90f,
                    sweepAngle = 360f * boundedProgress,
                    useCenter = true,
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height),
                )
            }
        }
    }
}