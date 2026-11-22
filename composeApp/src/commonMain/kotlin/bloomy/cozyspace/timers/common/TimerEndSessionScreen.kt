package bloomy.cozyspace.timers.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.components.LabelButton
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.calmCharacter
import cozyspace.composeapp.generated.resources.happyCharacter
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimerEndSessionScreen(
    isConcentration: Boolean,
    onRestart: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    val character = if (isConcentration) Res.drawable.happyCharacter else Res.drawable.calmCharacter
    val title = if (isConcentration) "Congratulations!" else "A nice, relaxing time"
    val message = if (isConcentration) {
        "You've made it to the end!"
    } else {
        "You can be proud of this moment you've set aside for yourself."
    }
    val buttonText = if (isConcentration) "Restart a session" else "Restart an exercise"

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val characterScale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.6f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 200)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(characterScale)
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(LightGreen.copy(alpha = 0.18f))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                        .background(LightGreen.copy(alpha = 0.35f))
                ) {
                    Image(
                        painter = painterResource(character),
                        contentDescription = "Character",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp)
                            .border(4.dp, DarkGreen, CircleShape)
                            .clip(CircleShape)
                            .background(WhiteBackground)
                            .padding(16.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.alpha(contentAlpha),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    color = DarkGreen,
                    fontWeight = FontWeight.Black,
                    fontSize = 36.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = message,
                    color = DarkGreen.copy(alpha = 0.7f),
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .alpha(contentAlpha)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LabelButton(
                text = buttonText,
                contentColor = WhiteBackground,
                backgroundColor = DarkGreen,
                modifier = Modifier.fillMaxWidth(),
                onClick = onRestart
            )

            LabelButton(
                text = "Back to timers",
                contentColor = DarkGreen,
                backgroundColor = WhiteBackground,
                modifier = Modifier.fillMaxWidth(),
                onClick = onHome
            )
        }
    }
}
