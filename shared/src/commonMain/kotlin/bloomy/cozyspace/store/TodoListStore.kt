package bloomy.cozyspace.store

import bloomy.cozyspace.data.dto.TodoRequestDto
import bloomy.cozyspace.domain.Todo
import com.arkivanov.mvikotlin.core.store.Store

interface TodoListStore : Store<TodoListStore.Intent, TodoListStore.State, TodoListStore.Label> {
    sealed interface Intent {
        data object GetTodoList : Intent
        data class CreateTodo(val todo: TodoRequestDto) : Intent
        data class CompleteTodo(val todoId: String) : Intent
    }

    sealed interface Label {
        data class ShowError(val message: String): Label
    }

    data class State(
        val loading: Boolean = false,
        val todoList: List<Todo> = emptyList(),
        val error: String? = null
    )
}
