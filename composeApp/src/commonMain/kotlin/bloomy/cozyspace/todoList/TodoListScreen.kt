package bloomy.cozyspace.todoList

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.todoList.components.TodoCategory
import bloomy.cozyspace.todoList.components.TodoItem
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.CategoryName
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TodoListScreen() {
    val sdf = SimpleDateFormat("dd/MM")
    val currentDate = sdf.format(Date())

    var task by remember {
        mutableStateOf(
            Task(
                id = "1",
                name = "Task 1",
                frequency = 0,
                type = CategoryName.Kitchen,
                startDate = "04/05/2026 18:00",
                isDone = false
            )
        )
    }

    Column {
        Text(
            text = "Tasks",
            color = DarkGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            maxLines = 1
        )

        Text(
            text = currentDate,
            color = LightGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            maxLines = 1
        )

        TodoCategory(CategoryName.Kitchen, 1)

        TodoItem(
            task = task,
            onTaskChecked = { checked ->
                task = task.copy(isDone = checked)
            }
        )
    }
}
