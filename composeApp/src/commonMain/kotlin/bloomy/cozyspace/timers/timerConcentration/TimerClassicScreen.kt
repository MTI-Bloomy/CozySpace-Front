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
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TimerClassicScreen(
    onTimerStop: () -> Unit,
    onFinish: () -> Unit = {},
    durationMinutes: Int = 15,
) {
    val timerDurationSeconds = durationMinutes.coerceAtLeast(1) * 60
    var remainingSeconds by rememberSaveable(durationMinutes) {
        androidx.compose.runtime.mutableIntStateOf(timerDurationSeconds)
    }
    var isRunning by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(isRunning) {
        while (isRunning && remainingSeconds > 0) {
            delay(1_000.milliseconds)
            if (isRunning && remainingSeconds > 0) remainingSeconds -= 1
        }
        if (remainingSeconds == 0) {
            isRunning = false
            onFinish()
        }
    }

    val elapsedProgress =
        (timerDurationSeconds - remainingSeconds).toFloat() / timerDurationSeconds
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
