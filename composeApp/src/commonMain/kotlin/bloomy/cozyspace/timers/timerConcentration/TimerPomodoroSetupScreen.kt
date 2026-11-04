package bloomy.cozyspace.timers.timerConcentration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.BreakActions
import bloomy.cozyspace.timers.components.DurationPicker
import bloomy.cozyspace.timers.components.SessionTaskList
import bloomy.cozyspace.timers.components.TimerSettingsBlock
import bloomy.cozyspace.timers.components.TimerSetupHeader

@Composable
fun TimerPomodoroSetupScreen(
    onBack: () -> Unit = {},
    onStartTimer: (Int) -> Unit = {},
) {
    var focusDuration by remember { mutableIntStateOf(25) }
    var breakDuration by remember { mutableIntStateOf(5) }
    var tasks by remember { mutableStateOf(emptyList<String>()) }
    var selectedBreakActions by remember { mutableStateOf(emptySet<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        TimerSetupHeader(
            title = "Pomodoro",
            subtitle = "Plan focused work and a restorative break.",
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TimerSettingsBlock(
                title = "Focus session",
                modifier = Modifier.weight(1f),
                compact = true,
            ) {
                DurationPicker(
                    title = "",
                    durationMinutes = focusDuration,
                    onDurationChange = { focusDuration = it },
                    showContainer = false,
                    compact = true,
                )
                SessionTaskList(
                    tasks = tasks,
                    onTasksChange = { tasks = it },
                    showContainer = false,
                    compact = true,
                    showDescription = false,
                )
            }

            TimerSettingsBlock(
                title = "Have a break",
                modifier = Modifier.weight(1f),
                compact = true,
            ) {
                DurationPicker(
                    title = "",
                    durationMinutes = breakDuration,
                    onDurationChange = { breakDuration = it },
                    maxHours = 2,
                    showContainer = false,
                    compact = true,
                )
                BreakActions(
                    selectedActions = selectedBreakActions,
                    onSelectionChange = { selectedBreakActions = it },
                    showContainer = false,
                    showTitle = false,
                    compact = true,
                )
            }
        }

        Button(
            onClick = { onStartTimer(focusDuration) },
            enabled = focusDuration > 0,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = WhiteBackground,
            ),
        ) {
            Text("Start timer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
