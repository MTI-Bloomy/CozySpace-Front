package bloomy.cozyspace.timers.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private enum class BreathPhase(val label: String, val expanded: Boolean) {
    Inhale("Inhale", true),
    HoldFull("Hold", true),
    Exhale("Exhale", false),
    HoldEmpty("Hold", false)
}

private const val PHASE_SECONDS = 4

@Composable
fun BoxBreathingComponent(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DarkGreen,
    contentColor: Color = WhiteBackground,
) {
    var phase by remember { mutableStateOf(BreathPhase.Inhale) }
    var secondsLeft by remember { mutableIntStateOf(PHASE_SECONDS) }

    // Loop of 4 phases for 4 seconds each
    LaunchedEffect(Unit) {
        while (true) {
            for (p in BreathPhase.entries) {
                phase = p
                for (s in PHASE_SECONDS downTo 1) {
                    secondsLeft = s
                    delay(1000.milliseconds)
                }
            }
        }
    }

    // The circle only moves during inhale/exhale
    val scale by animateFloatAsState(
        targetValue = if (phase.expanded) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = PHASE_SECONDS * 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    val isHolding = phase == BreathPhase.HoldFull || phase == BreathPhase.HoldEmpty
    val haloAlpha by animateFloatAsState(
        targetValue = if (isHolding) 0.35f else 0.18f,
        animationSpec = tween(600),
        label = "haloAlpha"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(320.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .background(backgroundColor.copy(alpha = haloAlpha), CircleShape)
            )
            Box(
                Modifier
                    .fillMaxSize(0.82f)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .background(backgroundColor.copy(alpha = haloAlpha + 0.12f), CircleShape)
            )
            Box(
                Modifier
                    .fillMaxSize(0.64f)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .shadow(12.dp, CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(backgroundColor.copy(alpha = 0.85f), backgroundColor)
                        ),
                        CircleShape
                    )
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Crossfade(targetState = phase, label = "phaseLabel") { p ->
                    Text(
                        text = p.label,
                        color = contentColor,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
