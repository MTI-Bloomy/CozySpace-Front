package bloomy.cozyspace.timers.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.components.LabelButton
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CharacterInteraction(
    character: DrawableResource,
    question: String,
    confirmAnswer: String,
    cancelAnswer: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {

    Row (
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(MidDarkGreen)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Image(
            painter = painterResource(character),
            contentDescription = "Character",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(88.dp)
                .border(3.dp, LightGreen, CircleShape)
                .clip(CircleShape)
                .background(LightGreen)
                .padding(10.dp)
        )

        Column (
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = question,
                color = WhiteBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LabelButton(
                    text = cancelAnswer,
                    contentColor = WhiteBackground,
                    backgroundColor = DarkGreen,
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )

                LabelButton(
                    text = confirmAnswer,
                    contentColor = DarkGreen,
                    backgroundColor = WhiteBackground,
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm
                )
            }
        }
    }
}
