package bloomy.cozyspace.timers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.arrow_back_ios
import org.jetbrains.compose.resources.painterResource

@Composable
fun ReturnButton(
    modifier: Modifier = Modifier,
    text: String,
    contentColor: Color = DarkGreen,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .clip(CircleShape)
            .background(contentColor.copy(alpha = 0.1f))
            .clickable(onClick = onClick)
            .padding(start = 10.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.arrow_back_ios),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            maxLines = 1
        )
    }
}
