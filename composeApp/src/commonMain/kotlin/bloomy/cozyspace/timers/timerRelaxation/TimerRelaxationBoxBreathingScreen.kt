package bloomy.cozyspace.timers.timerRelaxation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.BoxBreathingComponent
import bloomy.cozyspace.timers.components.CancelButton
import bloomy.cozyspace.timers.components.CharacterInteraction
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.happyCharacter
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.time.Duration.Companion.minutes

private val QUESTION_INTERVAL = 1.minutes // TODO => change to 5.minutes

@Composable
fun TimerRelaxationBoxBreathingScreen(
    onBack: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    var showQuestion by remember { mutableStateOf(false) }

    // showQuestion changes :
    // - at the start of the exercise (false) => waiting for 5 minutes, then showing the popup
    // - when closing the popup (false) => waiting for 5 minutes
    // - when the popup is shown (true) => waiting for the popup to be closed
    LaunchedEffect(showQuestion) {
        if (!showQuestion) {
            delay(QUESTION_INTERVAL)
            showQuestion = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        BoxBreathingComponent(
            modifier = Modifier
                .fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = showQuestion,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                CharacterInteraction(
                    character = Res.drawable.happyCharacter,
                    question = "Are you feeling better ?",
                    confirmAnswer = "Yes",
                    cancelAnswer = "No",
                    onConfirm = onFinish,
                    onCancel = { showQuestion = false },
                )
            }

            CancelButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            )
        }
    }
}
