package bloomy.cozyspace.timers.timerConcentration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun TimerConcentrationScreen(
    stores: Stores,
    fromTimerConcentration_toTimerClassic: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = "Concentration",
            color = DarkGreen,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
        )

        Button(
            onClick = fromTimerConcentration_toTimerClassic,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .heightIn(min = 72.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = WhiteBackground,
            ),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Timer classique", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text("Compte à rebours simple", fontSize = 14.sp)
            }
        }
    }
}
