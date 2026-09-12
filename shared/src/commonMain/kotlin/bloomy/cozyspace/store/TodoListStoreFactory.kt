package bloomy.cozyspace.store

import bloomy.cozyspace.data.TodoListRepository
import bloomy.cozyspace.data.dto.toDomain
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class TodoListStoreFactory(
    private val repository: TodoListRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) {
    suspend fun create(): TodoListStore {
        val initialState = TodoListStore.State(
            todoList = emptyList()
        )

        return object : TodoListStore,
            Store<TodoListStore.Intent, TodoListStore.State, TodoListStore.Label> by storeFactory.create(
                name = "TodoListStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class GetTodoListSuccess(val todoList: List<Todo>) : Msg
        data class CreateTodoSuccess(val todo: Todo) : Msg
        data class CompleteTodoSuccess(val initId: String) : Msg
        data class ModifyTodoSuccess(val todo: Todo) : Msg
        data class DeleteTodoSuccess(val deleteId: String) : Msg
        data class Error(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
        TodoListStore.Intent,
        Unit,
        TodoListStore.State,
        Msg,
        TodoListStore.Label
        >() {

        override fun executeIntent(intent: TodoListStore.Intent) {
            when (intent) {
                is TodoListStore.Intent.GetTodoList -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getTodoList()) {
                            is ApiResult.Success -> {
                                dispatch(Msg.GetTodoListSuccess(result.data.map { it.toDomain() }))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(TodoListStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is TodoListStore.Intent.CreateTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.createTodo(intent.todo)) {
                            is ApiResult.Success -> {
                                dispatch(Msg.CreateTodoSuccess(result.data.toDomain()))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(TodoListStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is TodoListStore.Intent.CompleteTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.completeTodo(intent.todoId)) {
                            is ApiResult.Success -> {
                                dispatch(Msg.CompleteTodoSuccess(intent.todoId))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(TodoListStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is TodoListStore.Intent.ModifyTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.modifyTodo(intent.todoId, intent.todo)) {
                            is ApiResult.Success -> {
                                dispatch(Msg.ModifyTodoSuccess(result.data.toDomain()))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(TodoListStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is TodoListStore.Intent.DeleteTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.deleteTodo(intent.todoId)) {
                            is ApiResult.Success -> {
                                dispatch(Msg.DeleteTodoSuccess(intent.todoId))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(TodoListStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<TodoListStore.State, Msg> {
        override fun TodoListStore.State.reduce(msg: Msg): TodoListStore.State {
            return when (msg) {
                is Msg.Loading -> copy(
                    loading = true,
                    error = null
                )

                is Msg.GetTodoListSuccess -> copy(
                    loading = false,
                    todoList = msg.todoList,
                    error = null
                )

                is Msg.CreateTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList + msg.todo,
                    error = null
                )

                is Msg.CompleteTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList.filter { it.id != msg.initId },
                    error = null
                )

                is Msg.ModifyTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList.map { todo ->
                        if (todo.id == msg.todo.id) msg.todo else todo
                    },
                    error = null
                )

                is Msg.DeleteTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList.filter { it.id != msg.deleteId },
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
