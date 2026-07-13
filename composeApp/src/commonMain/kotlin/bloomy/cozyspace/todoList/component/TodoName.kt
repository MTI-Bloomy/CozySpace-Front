package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.CategoryName
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.delete
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoName(task: Todo, onCategorySelected: (Category) -> Unit) {
    var todoName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(
        when (task.type) {
            RoomType.KITCHEN -> Category.Kitchen
            RoomType.BATHROOM -> Category.Bathroom
            RoomType.BEDROOM -> Category.Bedroom
            RoomType.GARDEN -> Category.Garden
            RoomType.WORK -> Category.Work
        }
    )}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGreen,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = todoName,
                    onValueChange = { todoName = it },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = task.name,
                            color = WhiteBackground.copy(alpha = 0.7f),
                            fontSize = 20.sp,
                            maxLines = 1
                        )
                    },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = LightGreen,
                        unfocusedContainerColor = LightGreen.copy(alpha = 0.85f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = WhiteBackground,
                        unfocusedTextColor = WhiteBackground,
                        focusedPlaceholderColor = WhiteBackground.copy(alpha = 0.7f),
                        unfocusedPlaceholderColor = WhiteBackground.copy(alpha = 0.7f),
                        cursorColor = WhiteBackground
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(WhiteBackground)
                        .clickable { /* delete action */ }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.delete),
                        contentDescription = "Delete",
                        tint = DarkGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            CategoryDropdown(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    onCategorySelected(category)
                }
            )
        }
    }
}
