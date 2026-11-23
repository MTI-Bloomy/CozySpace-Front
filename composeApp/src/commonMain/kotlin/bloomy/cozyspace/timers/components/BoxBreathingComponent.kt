package bloomy.cozyspace.timers.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground

private enum class BreathPhase(val label: String, val expanded: Boolean) {
    Inhale("Inhale", true),
    HoldFull("Hold", true),
    Exhale("Exhale", false),
    HoldEmpty("Hold", false)
}

private const val PHASE_SECONDS = 4
private const val MIN_SCALE = 0.6f

private val REFERENCE_SIZE = 340.dp   // Reference size for the square
private val MAX_SIZE = 420.dp         // Maximum size for the square
private val MIN_MARGIN = 16.dp        // Minimal margin around the square

@Composable
fun BoxBreathingComponent(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DarkGreen,
    contentColor: Color = WhiteBackground,
) {
    // 0f..4f : the integer part corresponds to the phase, and the decimal part corresponds to the progress within the phase
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = BreathPhase.entries.size.toFloat(),
                animationSpec = tween(
                    durationMillis = BreathPhase.entries.size * PHASE_SECONDS * 1000, // Duration of the whole cycle = 16s
                    easing = LinearEasing
                )
            )
        }
    }

    // Corresponds to the phase of the box breathing
    val phase by remember {
        derivedStateOf {
            val index = progress.value.toInt().coerceIn(0, BreathPhase.entries.lastIndex)
            BreathPhase.entries[index]
        }
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Side of the square : we remove the size of the margin
        val side = (minOf(maxWidth, maxHeight) - MIN_MARGIN * 2).coerceIn(0.dp, MAX_SIZE)

        Box(
            modifier = Modifier.size(side),
            contentAlignment = Alignment.Center
        ) {
            // Square with point
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            ) {
                val inset = 20.dp.toPx()
                val strokeWidth = 4.dp.toPx()

                val corners = listOf(
                    Offset(inset, size.height - inset),             // Bottom left
                    Offset(inset, inset),                           // Top left
                    Offset(size.width - inset, inset),              // Top right
                    Offset(size.width - inset, size.height - inset),// Bottom right
                )

                // Dot position
                val current = progress.value
                val dot = lerp(
                    corners[current.toInt().coerceIn(0, 3)],
                    corners[(current.toInt().coerceIn(0, 3) + 1) % 4],
                    current - current.toInt().coerceIn(0, 3)
                )

                val baseAlpha = 0.15f
                val trailLength = 1.5f

                // Square
                for (i in 0 until 4) {
                    drawLine(
                        color = backgroundColor.copy(alpha = baseAlpha),
                        start = corners[i],
                        end = corners[(i + 1) % 4],
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                        blendMode = BlendMode.Src
                    )
                }

                // Trail
                val lo = current - trailLength
                for (i in 0 until 4) {
                    for (shift in intArrayOf(-4, 0, 4)) {
                        val sideStart = (i + shift).toFloat()
                        val qa = maxOf(lo, sideStart)
                        val qb = minOf(current, sideStart + 1f)
                        if (qb - qa <= 0.0001f) continue

                        val a = lerp(corners[i], corners[(i + 1) % 4], qa - sideStart)
                        val b = lerp(corners[i], corners[(i + 1) % 4], qb - sideStart)

                        // Opacity depending on the distance to the dot
                        val stops = Array(9) { s ->
                            val f = s / 8f
                            val q = qa + f * (qb - qa)
                            val fade = 1f - (current - q) / trailLength
                            val alpha = baseAlpha + (1f - baseAlpha) * fade * fade
                            f to backgroundColor.copy(alpha = alpha)
                        }

                        // Line that follows the gradation from a to b
                        drawLine(
                            brush = Brush.linearGradient(colorStops = stops, start = a, end = b),
                            start = a,
                            end = b,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round,
                            blendMode = BlendMode.Src
                        )
                    }
                }

                // Dot with halo
                drawCircle(color = backgroundColor.copy(alpha = 0.25f), radius = 18.dp.toPx(), center = dot)
                drawCircle(color = backgroundColor, radius = 10.dp.toPx(), center = dot)
                drawCircle(color = contentColor, radius = 4.dp.toPx(), center = dot)
            }
        }

        // Circle inside the square
        Box(
            modifier = Modifier.size(side * (220f / 340f)),
            contentAlignment = Alignment.Center
        ) {
            val scale = {
                val index = progress.value.toInt().coerceIn(0, 3)
                val t = (progress.value - index).coerceIn(0f, 1f)
                val eased = FastOutSlowInEasing.transform(t)
                when (BreathPhase.entries[index]) {
                    BreathPhase.Inhale -> lerp(MIN_SCALE, 1f, eased)
                    BreathPhase.HoldFull -> 1f
                    BreathPhase.Exhale -> lerp(1f, MIN_SCALE, eased)
                    BreathPhase.HoldEmpty -> MIN_SCALE
                }
            }

            val isHolding = phase == BreathPhase.HoldFull || phase == BreathPhase.HoldEmpty
            val haloAlpha by animateFloatAsState(
                targetValue = if (isHolding) 0.35f else 0.18f,
                animationSpec = tween(600),
                label = "haloAlpha"
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { scaleX = scale(); scaleY = scale() }
                    .background(backgroundColor.copy(alpha = haloAlpha), CircleShape)
            )
            Box(
                Modifier
                    .fillMaxSize(0.82f)
                    .graphicsLayer { scaleX = scale(); scaleY = scale() }
                    .background(backgroundColor.copy(alpha = haloAlpha + 0.12f), CircleShape)
            )
            Box(
                Modifier
                    .fillMaxSize(0.64f)
                    .graphicsLayer { scaleX = scale(); scaleY = scale() }
                    .shadow(12.dp, CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(backgroundColor.copy(alpha = 0.85f), backgroundColor)
                        ),
                        CircleShape
                    )
            )

            // Text inside the circle
            Crossfade(targetState = phase, label = "phaseLabel") { p ->
                Text(
                    text = p.label,
                    color = contentColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
