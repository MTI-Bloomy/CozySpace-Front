package bloomy.cozyspace

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import bloomy.cozyspace.theme.AppTheme
import bloomy.cozyspace.todoList.TodoListScreen

@Composable
fun App() {
    AppTheme {
        TodoListScreen()
    }
}
