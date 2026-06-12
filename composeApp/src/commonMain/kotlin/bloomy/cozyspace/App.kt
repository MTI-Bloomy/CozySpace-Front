package bloomy.cozyspace

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import bloomy.cozyspace.todoList.components.TodoItem
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.CategoryName

@Composable
@Preview
fun App() {
    MaterialTheme {
        TodoItem(Task(id = "1", name = "Task 1", frequency = 0, type = CategoryName.Kitchen, startDate = ""), clicked = {})
    }
}
