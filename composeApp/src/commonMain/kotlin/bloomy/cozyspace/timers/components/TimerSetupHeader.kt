package bloomy.cozyspace.timers.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import bloomy.cozyspace.theme.DarkGreen

@Composable
fun TimerSetupHeader(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        ReturnButton(
            text = "Focus methods",
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start),
        )
        Text(title, color = DarkGreen, fontWeight = FontWeight.Black, fontSize = 24.sp)
        Text(subtitle, color = DarkGreen.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}
