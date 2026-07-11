package bloomy.cozyspace.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.auth.signUp.getPasswordStrength
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.MidLightGreen
import bloomy.cozyspace.theme.Orange
import bloomy.cozyspace.theme.Red

@Composable
fun PasswordStrengthBar(password: String) {
    val strength = getPasswordStrength(password)

    val colors = listOf(
        Color.LightGray,
        Red,
        Orange,
        MidLightGreen,
        DarkGreen
    )

    val labels = listOf(
        "Password strength",
        "Very weak",
        "Weak",
        "Medium",
        "Strong"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in 1..4) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (i <= strength) colors[strength]
                            else Color.LightGray.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // TEXT
        Text(
            text = labels[strength],
            modifier = Modifier.align(Alignment.End),
            fontSize = 12.sp,
            color = colors[strength]
        )
    }
}
