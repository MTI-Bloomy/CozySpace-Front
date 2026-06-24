package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.MidLightGreen
import bloomy.cozyspace.theme.ShadowColor
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoAdd
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddTodoButton(onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Box (
        modifier = modifier
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 8.dp,
                    spread = 0.dp,
                    color = ShadowColor,
                    offset = DpOffset(x = 0.dp, 4.dp)
                )
            )
            .clip(CircleShape)
            .background(DarkGreen)
            .border(2.dp, MidLightGreen, CircleShape)
            .clickable(onClick = onClick),
    ) {
        Icon(
            painter = painterResource(Res.drawable.todoAdd),
            contentDescription = "Add new task",
            tint = MidLightGreen,
            modifier = Modifier.size(50.dp)
        )
    }
}
