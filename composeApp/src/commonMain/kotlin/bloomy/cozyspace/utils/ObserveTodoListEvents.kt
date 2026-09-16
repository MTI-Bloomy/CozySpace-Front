package bloomy.cozyspace.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoDoneStore
import bloomy.cozyspace.store.TodoListStore
import com.arkivanov.mvikotlin.core.rx.observer

@Composable
fun ObserveTodoListEvents(stores: Stores) {
    DisposableEffect(stores.todoList) {
        val disposable = stores.todoList.labels(
            observer { label ->
                when (label) {
                    is TodoListStore.Label.TodoCompleted -> {
                        stores.todoDone.accept(TodoDoneStore.Intent.AddTodoDone(label.todo))
                    }

                    else -> Unit
                }
            },
        )
        onDispose { disposable.dispose() }
    }
}
