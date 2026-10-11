package bloomy.cozyspace.timers.timerRelaxation

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.CancelButton
import bloomy.cozyspace.timers.components.CharacterInteraction
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.happyCharacter

@Composable
fun TimerRelaxationIntrospectionScreen(
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Think of 5 positive statements about yourself.\n" +
                "Then, repeat them out loud.",
            color = DarkGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 42.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CharacterInteraction(
                character = Res.drawable.happyCharacter,
                question = "Are you feeling better ?",
                confirmAnswer = "Yes",
                cancelAnswer = "No",
                onConfirm = onBack,
                onCancel = { },
            )

            CancelButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            )
        }
    }
}
