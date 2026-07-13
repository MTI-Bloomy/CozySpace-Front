package bloomy.cozyspace.todoList.component.microComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.ShadowColor

@Composable
fun LabelButton(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit = {}
) {
    Box (
        modifier = modifier
            .dropShadow(
                shape = RoundedCornerShape(10.dp),
                shadow = Shadow(
                    radius = 8.dp,
                    spread = 0.dp,
                    color = ShadowColor,
                    offset = DpOffset(x = 0.dp, 4.dp)
                )
            )
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(2.dp, contentColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            maxLines = 1
        )
    }
}
