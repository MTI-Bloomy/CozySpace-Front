package bloomy.cozyspace.timers.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.pause
import cozyspace.composeapp.generated.resources.play_arrow
import cozyspace.composeapp.generated.resources.stop
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimerControls(
    isRunning: Boolean,
    onPauseResume: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimerControlButton(
            icon = painterResource(if (isRunning) Res.drawable.pause else Res.drawable.play_arrow),
            description = if (isRunning) "Pause" else "Reprendre",
            enabled = true,
            onClick = onPauseResume,
        )

        TimerControlButton(
            icon = painterResource(Res.drawable.stop),
            description = "Arrêter",
            enabled = true,
            onClick = onStop,
        )
    }
}

@Composable
private fun TimerControlButton(
    icon: Painter,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .width(72.dp)
            .semantics { contentDescription = description },
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkGreen,
            contentColor = WhiteBackground,
        ),
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = WhiteBackground,
        )
    }
}