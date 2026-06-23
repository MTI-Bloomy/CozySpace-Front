package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.utils.CornerTriangleShape
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoCornerDone
import cozyspace.composeapp.generated.resources.todoCornerList
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoCorner(isTodoList: Boolean, mirrored: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box (
        modifier = modifier
            .size(96.dp)
            .clip(CornerTriangleShape(cornerRadius = 12.dp, mirrored = mirrored))
            .background(DarkGreen)
            .border(2.dp, LightGreen, CornerTriangleShape(cornerRadius = 12.dp, mirrored = mirrored))
            .clickable(onClick = onClick),
    ) {
        Icon(
            painter = painterResource(
                if (isTodoList) Res.drawable.todoCornerDone
                else Res.drawable.todoCornerList
            ),
            contentDescription = "Corner button to access",
            tint = WhiteBackground,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    x = if (mirrored) 48.dp else 10.dp, // Hand placed
                    y = 10.dp
                )
                .size(40.dp),
        )
    }
}
