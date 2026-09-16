package bloomy.cozyspace.store

import bloomy.cozyspace.cache.TodoDoneCache
import bloomy.cozyspace.cache.TodoDoneStorage
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
    private val storage: TodoDoneStorage,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): TodoDoneStore {
        val cache = storage.get()

        val initialState = TodoDoneStore.State(
            todoDone = cache?.todoDone ?: emptyList(),
        )

        return object : TodoDoneStore,
            Store<TodoDoneStore.Intent, TodoDoneStore.State, TodoDoneStore.Label> by storeFactory.create(
                name = "TodoDoneStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data object Offline : Msg
        data class GetTodoDoneSuccess(val todoDone: List<Todo>) : Msg
        data class AddTodoDoneSuccess(val todo: Todo) : Msg
        data class Error(val message: String) : Msg
        data object Clear : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
        TodoDoneStore.Intent,
        Unit,
        TodoDoneStore.State,
        Msg,
        TodoDoneStore.Label,
        >() {

        override fun executeIntent(intent: TodoDoneStore.Intent) {
            when (intent) {
                is TodoDoneStore.Intent.GetTodoDone -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getTodoDone()) {
                            is ApiResult.Success -> {
                                val todoDone = result.data.map { it.toDomain() }

                                storage.save(
                                    TodoDoneCache(
                                        todoDone = todoDone,
                                    ),
                                )

                                dispatch(Msg.GetTodoDoneSuccess(todoDone))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoDoneStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                is TodoDoneStore.Intent.AddTodoDone -> {
                    scope.launch {
                        storage.save(
                            TodoDoneCache(
                                todoDone = state().todoDone + intent.todo,
                            ),
                        )

                        dispatch(Msg.AddTodoDoneSuccess(intent.todo))
                    }
                }

                TodoDoneStore.Intent.Clear -> dispatch(Msg.Clear)
            }
        }
    }

    private object ReducerImpl : Reducer<TodoDoneStore.State, Msg> {
        override fun TodoDoneStore.State.reduce(msg: Msg): TodoDoneStore.State {
            return when (msg) {
                Msg.Loading -> copy(
                    loading = true,
                    error = null,
                )

                Msg.Offline -> copy(
                    loading = false,
                    error = null,
                )

                is Msg.GetTodoDoneSuccess -> copy(
                    loading = false,
                    todoDone = msg.todoDone,
                    error = null,
                )

                is Msg.AddTodoDoneSuccess -> copy(
                    todoDone = todoDone + msg.todo,
                )

                is Msg.Error -> copy(
                    loading = false,
                    error = msg.message,
                )

                Msg.Clear -> TodoDoneStore.State()
            }
        }
    }
}
