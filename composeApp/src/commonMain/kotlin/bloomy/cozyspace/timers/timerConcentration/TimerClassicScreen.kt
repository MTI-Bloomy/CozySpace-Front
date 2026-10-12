package bloomy.cozyspace.timers.timerConcentration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.TimerCircle
import bloomy.cozyspace.timers.components.TimerControls
import kotlinx.coroutines.delay

private const val TIMER_DURATION_SECONDS = 15 * 60 // TODO: Retrieve time set in previous pages

@Composable
fun TimerClassicScreen(onTimerStop: () -> Unit) {
    var remainingSeconds by rememberSaveable { mutableIntStateOf(TIMER_DURATION_SECONDS) }
    var isRunning by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(isRunning) {
        while (isRunning && remainingSeconds > 0) {
            delay(1_000)
            if (isRunning && remainingSeconds > 0) remainingSeconds -= 1
        }
        if (remainingSeconds == 0) {
            isRunning = false
            // TODO: Navigate to the timer end page when it is implemented.
        }
    }

    val elapsedProgress =
        (TIMER_DURATION_SECONDS - remainingSeconds).toFloat() / TIMER_DURATION_SECONDS
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(horizontal = 24.dp, vertical = 20.dp),
    ) {
        val clockSize = minOf(maxWidth * 0.82f, maxHeight * 0.60f, 340.dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Timer",
                color = DarkGreen,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                TimerCircle(
                    progress = elapsedProgress,
                    modifier = Modifier.size(clockSize),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "%02d:%02d".format(minutes, seconds),
                    color = DarkGreen,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            TimerControls(
                isRunning = isRunning,
                onPauseResume = { isRunning = !isRunning },
                onTimerStop = {
                    isRunning = false
                    onTimerStop()
                },
            )
        }
    }
}