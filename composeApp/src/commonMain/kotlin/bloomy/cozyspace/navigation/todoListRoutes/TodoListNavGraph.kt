package bloomy.cozyspace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bloomy.cozyspace.navigation.todoListRoutes.TodoScreen
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.todoList.TodoListScreen
import bloomy.cozyspace.todoList.todoDone.TodoDoneScreen

@Composable
fun TodoListNavGraph (navController: NavHostController, stores: Stores) {

    NavHost(
        navController = navController,
        startDestination = TodoScreen.TodoList.route
    ) {
        composable(TodoScreen.TodoList.route) {
            TodoListScreen(
                fromTodoList_toTodoDone = {
                    navController.navigate(TodoScreen.TodoDone.route)
                },
                stores
            )
        }

        composable(TodoScreen.TodoDone.route) {
            TodoDoneScreen(
                fromTodoDone_toTodoList = {
                    navController.navigate(TodoScreen.TodoList.route)
                },
                stores
            )
        }
    }
}
