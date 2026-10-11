package bloomy.cozyspace.timers.timerRelaxation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.timers.components.CharacterInteraction
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.happyCharacter


@Composable
fun TimerRelaxationScreen(stores: Stores) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

        }

        CharacterInteraction(
            character = Res.drawable.happyCharacter,
            question = "Are you feeling better ?",
            confirmAnswer = "Yes",
            cancelAnswer = "No",
            modifier = Modifier
                .align(Alignment.BottomCenter),
            onConfirm = { },
            onDismiss = { }
        )
    }
}
