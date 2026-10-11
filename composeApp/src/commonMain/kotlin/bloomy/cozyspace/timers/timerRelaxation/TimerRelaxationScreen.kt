package bloomy.cozyspace.timers.timerRelaxation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.ReturnButton
import bloomy.cozyspace.timers.components.TimerCard
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.psychiatry
import cozyspace.composeapp.generated.resources.self_improvement
import cozyspace.composeapp.generated.resources.sentiment_calm
import cozyspace.composeapp.generated.resources.target_check


@Composable
fun TimerRelaxationScreen(
    stores: Stores,
    onBack: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp)
            .padding(vertical = 16.dp)
    ) {
        ReturnButton(
            text = "Back",
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Type of exercises",
                color = DarkGreen,
                fontWeight = FontWeight.Black,
                fontSize = 42.sp,
            )
            Text(
                text = "Relaxation timer",
                color = DarkGreen.copy(alpha = 0.6f),
                fontSize = 16.sp,
            )

            Spacer(Modifier.height(40.dp))

            TimerCard(
                icon = Res.drawable.self_improvement,
                iconDescription = "Introspection",
                title = "Introspection",
                description = "Reflect on a meaningful sentence, one at a time",
                backgroundColor = DarkGreen,
                contentColor = WhiteBackground,
                onClick = { },
            )

            Spacer(Modifier.height(16.dp))

            TimerCard(
                icon = Res.drawable.sentiment_calm,
                iconDescription = "Square breathing",
                title = "Square breathing",
                description = "Breathe in, hold, out, hold: 4 seconds each",
                backgroundColor = DarkGreen,
                contentColor = WhiteBackground,
                onClick = { }
            )
        }

//        CharacterInteraction(
//            character = Res.drawable.happyCharacter,
//            question = "Are you feeling better ?",
//            confirmAnswer = "Yes",
//            cancelAnswer = "No",
//            modifier = Modifier
//                .align(Alignment.BottomCenter),
//            onConfirm = { },
//            onDismiss = { }
//        )
    }
}
