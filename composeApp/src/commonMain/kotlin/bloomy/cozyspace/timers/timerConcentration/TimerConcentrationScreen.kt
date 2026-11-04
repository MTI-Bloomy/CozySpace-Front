package bloomy.cozyspace.timers.timerConcentration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.ReturnButton
import bloomy.cozyspace.timers.components.TimerCard
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.psychiatry
import cozyspace.composeapp.generated.resources.target_check

@Composable
fun TimerConcentrationScreen(
    onPomodoro: () -> Unit,
    onWorkTimer: () -> Unit,
    onBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ReturnButton(text = "Back", onClick = onBack, modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.weight(1f))
        Text(
            text = "Choose a focus method",
            color = DarkGreen,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = "Set up the kind of work session that suits you.",
            color = DarkGreen.copy(alpha = 0.7f),
            fontSize = 15.sp,
        )
        Spacer(Modifier.height(32.dp))
        TimerCard(
            icon = Res.drawable.target_check,
            iconDescription = "Pomodoro focus method",
            title = "Pomodoro",
            description = "Focus sessions with restorative breaks",
            backgroundColor = DarkGreen,
            contentColor = WhiteBackground,
            onClick = onPomodoro,
        )
        Spacer(Modifier.height(16.dp))
        TimerCard(
            icon = Res.drawable.psychiatry,
            iconDescription = "Simple work timer method",
            title = "Work timer",
            description = "A simple work session with your own task list",
            backgroundColor = DarkGreen,
            contentColor = WhiteBackground,
            onClick = onWorkTimer,
        )
        Spacer(Modifier.weight(1f))
    }
}
