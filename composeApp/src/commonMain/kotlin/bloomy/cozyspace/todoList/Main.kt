package bloomy.cozyspace.todoList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.navigation.TodoListNavGraph
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.utils.observeState

@Composable
fun TodoMain(stores: Stores) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val navTodoController = rememberNavController()
        TodoListNavGraph(navTodoController, stores)
    }
}
