package bloomy.cozyspace.store

import bloomy.cozyspace.data.TodoDoneRepository
import bloomy.cozyspace.data.dto.toDomain
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class TodoDoneStoreFactory(
    private val repository: TodoDoneRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) {
    suspend fun create(): TodoDoneStore {
        val initialState = TodoDoneStore.State(
            todoDone = emptyList()
        )

        return object : TodoDoneStore,
            Store<TodoDoneStore.Intent, TodoDoneStore.State, TodoDoneStore.Label> by storeFactory.create(
                name = "TodoDoneStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class GetTodoDoneSuccess(val todoDone: List<Todo>) : Msg
        data class Error(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
        TodoDoneStore.Intent,
        Unit,
        TodoDoneStore.State,
        Msg,
        TodoDoneStore.Label
        >() {

        override fun executeIntent(intent: TodoDoneStore.Intent) {
            when (intent) {
                is TodoDoneStore.Intent.GetTodoDone -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getTodoDone()) {
                            is ApiResult.Success -> {
                                dispatch(Msg.GetTodoDoneSuccess(result.data.map { it.toDomain() }))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoDoneStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(TodoDoneStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<TodoDoneStore.State, Msg> {
        override fun TodoDoneStore.State.reduce(msg: Msg): TodoDoneStore.State {
            return when (msg) {
                is Msg.Loading -> copy(
                    loading = true,
                    error = null
                )

                is Msg.GetTodoDoneSuccess -> copy(
                    loading = false,
                    todoDone = msg.todoDone,
                    error = null
                )

                is Msg.Error -> copy(
                    loading = false,
                    error = msg.message
                )
            }
        }
    }
}
