package bloomy.cozyspace.store

import bloomy.cozyspace.data.dto.TodoRequestDto
import bloomy.cozyspace.domain.Todo
import com.arkivanov.mvikotlin.core.store.Store

interface TodoDoneStore : Store<TodoDoneStore.Intent, TodoDoneStore.State, TodoDoneStore.Label> {
    sealed interface Intent {
        data object GetTodoDone : Intent
    }

    sealed interface Label {
        data class ShowError(val message: String): Label
    }

    data class State(
        val loading: Boolean = false,
        val todoDone: List<Todo> = emptyList(),
        val error: String? = null
    )
}
