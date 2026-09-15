package bloomy.cozyspace.home.components.microComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.House
import bloomy.cozyspace.home.components.utils.toDisplayDate
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground

@Composable
fun SaveItem(
    house: House,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LightGreen, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(
            text = "Sauvegarde du ${house.saveDate?.toDisplayDate() ?: "--/--/----"}",
            color = WhiteBackground,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )

        Text(
            text = "${house.rooms.size} pièce${if (house.rooms.size > 1) "s" else ""}",
            color = WhiteBackground.copy(alpha = 0.85f),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )

        Button(
            onClick = onViewClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = WhiteBackground,
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.6f),
        ) {
            Text("Voir")
        }
    }
}
