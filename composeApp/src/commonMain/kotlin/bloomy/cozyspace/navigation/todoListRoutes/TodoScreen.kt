package bloomy.cozyspace.navigation.todoListRoutes

sealed class TodoScreen(val route: String) {
    data object TodoList : TodoScreen("todo_list")
    data object TodoDone : TodoScreen("todo_done")
}
