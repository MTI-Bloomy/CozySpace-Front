package bloomy.cozyspace.timers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.close
import org.jetbrains.compose.resources.painterResource

@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DarkGreen,
    contentColor: Color = WhiteBackground,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(2.dp, contentColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.close),
            contentDescription = "Close button",
            tint = contentColor,
            modifier = Modifier.size(32.dp)
        )
    }
}
