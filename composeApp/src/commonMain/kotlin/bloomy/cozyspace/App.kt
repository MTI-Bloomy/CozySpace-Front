package bloomy.cozyspace

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import bloomy.cozyspace.theme.AppTheme
import bloomy.cozyspace.todoList.TodoListScreen
import bloomy.cozyspace.todoList.components.TodoItem
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.CategoryName

@Composable
@Preview
fun App() {
    AppTheme {
        TodoListScreen()
    }
}
