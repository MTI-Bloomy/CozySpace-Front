package bloomy.cozyspace.timers.timerWork

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import bloomy.cozyspace.timers.components.DurationPicker
import bloomy.cozyspace.timers.components.SessionTaskList
import bloomy.cozyspace.timers.components.TimerSettingsBlock
import bloomy.cozyspace.timers.components.TimerSetupHeader

@Composable
fun TimerWorkScreen(
    onBack: () -> Unit = {},
    onStartTimer: (Int) -> Unit = {},
) {
    var workDuration by remember { mutableIntStateOf(60) }
    var tasks by remember { mutableStateOf(emptyList<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        TimerSetupHeader(
            title = "Work timer",
            subtitle = "Set a timer and make a list for this work session.",
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TimerSettingsBlock(title = "Work session") {
                DurationPicker(
                    title = "",
                    durationMinutes = workDuration,
                    onDurationChange = { workDuration = it },
                    showContainer = false,
                )
                SessionTaskList(
                    tasks = tasks,
                    onTasksChange = { tasks = it },
                    showContainer = false,
                )
            }
        }

        Button(
            onClick = { onStartTimer(workDuration) },
            enabled = workDuration > 0,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = WhiteBackground,
            ),
        ) {
            Text("Start timer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
