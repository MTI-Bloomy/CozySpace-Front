package bloomy.cozyspace

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.navigation.TodoListNavGraph
import bloomy.cozyspace.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()
        TodoListNavGraph(navController)
    }
}
