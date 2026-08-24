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
import bloomy.cozyspace.theme.Red
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.popUp.TaskDeletePopup
import bloomy.cozyspace.todoList.popUp.TaskDetailsPopup
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.CategoryName
import bloomy.cozyspace.todoList.utils.toCategory
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.delete
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoName(
    task: Todo,
    onNameChanged: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onDelete: (String) -> Unit,
) {
    var todoName by remember(task.id) { mutableStateOf(task.name) }
    var selectedCategory by remember(task.id) { mutableStateOf(task.type.toCategory()) }

    val isError = todoName.isBlank()
    var showDeletePopup by remember { mutableStateOf(false) }

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
                Column(modifier = Modifier.weight(1f)) {
                    TextField(
                        value = todoName,
                        onValueChange = {
                            todoName = it
                            onNameChanged(it)
                        },
                        singleLine = true,
                        isError = isError,
                        modifier = Modifier.fillMaxWidth(),
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
                            cursorColor = WhiteBackground,

                            errorContainerColor = LightGreen.copy(alpha = 0.85f),
                            errorIndicatorColor = Color.Transparent,
                            errorTextColor = WhiteBackground,
                            errorPlaceholderColor = WhiteBackground.copy(alpha = 0.7f),
                            errorCursorColor = WhiteBackground
                        )
                    )
                    if (isError) {
                        Text(
                            text = "A task's name cannot be blank",
                            color = Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(WhiteBackground)
                        .clickable { showDeletePopup = true }
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

    if (showDeletePopup) {
        TaskDeletePopup(
            task = task,
            onDismiss = { showDeletePopup = false },
            onConfirm = {
                onDelete(task.id)
                showDeletePopup = false
            }
        )
    }
}
