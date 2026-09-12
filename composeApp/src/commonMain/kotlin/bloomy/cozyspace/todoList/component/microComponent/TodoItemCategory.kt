package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.CategoryName

@Composable
fun TodoItemCategory(type: CategoryName) {
    val colors = Category.valueOf(type.name).colors

    Surface(
        shape = RoundedCornerShape(50),
        color = WhiteBackground,
        border = BorderStroke(1.dp, colors.dark)
    ) {
        Text(
            text = type.name,
            color = colors.dark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            maxLines = 1
        )
    }
}
