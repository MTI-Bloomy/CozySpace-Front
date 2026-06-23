package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.MidLightGreen
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoAdd
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddTodoButton(onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Box (
        modifier = modifier
            .clip(CircleShape)
            .background(DarkGreen)
            .border(2.dp, MidLightGreen, CircleShape)
    ) {
        Icon(
            painter = painterResource(Res.drawable.todoAdd),
            contentDescription = "Add new task",
            tint = MidLightGreen,
            modifier = Modifier.size(50.dp)
        )
    }
}
