package bloomy.cozyspace.timers.timerMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoListStore
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.timerMain.components.TimerCard
import bloomy.cozyspace.utils.observeState
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.add
import cozyspace.composeapp.generated.resources.psychiatry
import cozyspace.composeapp.generated.resources.target_check
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimerMainScreen(
    fromTimerMain_toTimerConcentration: () -> Unit = {},
    fromTimerMain_toTimerRelaxation: () -> Unit = {},
    stores: Stores
) {
    val timerState = stores.todoList.observeState()

    LaunchedEffect(Unit) {
        stores.todoList.accept(TodoListStore.Intent.GetTodoList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Timers",
            color = DarkGreen,
            fontWeight = FontWeight.Black,
            fontSize = 42.sp,
        )
        Text(
            text = "Go at your own pace",
            color = DarkGreen.copy(alpha = 0.6f),
            fontSize = 16.sp,
        )

        Spacer(Modifier.height(40.dp))

        TimerCard(
            icon = Res.drawable.target_check,
            iconDescription = "Concentration timer",
            title = "Concentration",
            description = "Focused sessions with breaks",
            backgroundColor = DarkGreen,
            contentColor = WhiteBackground,
            onClick = fromTimerMain_toTimerConcentration
        )

        Spacer(Modifier.height(16.dp))

        TimerCard(
            icon = Res.drawable.psychiatry,
            iconDescription = "Relaxation timer",
            title = "Relaxation",
            description = "Take a breath",
            backgroundColor = DarkGreen,
            contentColor = WhiteBackground,
            onClick = fromTimerMain_toTimerRelaxation
        )
    }
}
