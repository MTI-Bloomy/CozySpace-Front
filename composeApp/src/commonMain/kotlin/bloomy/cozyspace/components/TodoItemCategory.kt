package bloomy.cozyspace.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.utils.Category
import bloomy.cozyspace.utils.CategoryName

@Composable
fun TodoItemCategory(type: CategoryName) {
    val colors = Category.valueOf(type.name).colors

    val shape = RoundedCornerShape(60)
    Card(
        modifier = Modifier
            .wrapContentSize().border(shape = shape, width = 2.dp, color = colors.dark),
        shape = shape,
        //elevation = 10.dp, TODO
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .wrapContentSize(),
        ) {
            Text(
                text = type.name,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.W800,
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
                maxLines = 1
            )
        }
    }
}
