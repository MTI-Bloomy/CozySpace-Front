package bloomy.cozyspace.store

import bloomy.cozyspace.cache.TodoListCache
import bloomy.cozyspace.cache.TodoListStorage
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
    private val storage: TodoListStorage,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): TodoListStore {
        val cache = storage.get()

        val initialState = TodoListStore.State(
            todoList = cache?.todoList ?: emptyList(),
        )

        return object : TodoListStore,
            Store<TodoListStore.Intent, TodoListStore.State, TodoListStore.Label> by storeFactory.create(
                name = "TodoListStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data object Offline : Msg
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
        TodoListStore.Label,
        >() {

        override fun executeIntent(intent: TodoListStore.Intent) {
            when (intent) {
                is TodoListStore.Intent.GetTodoList -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getTodoList()) {
                            is ApiResult.Success -> {
                                val todoList = result.data.map { it.toDomain() }

                                dispatch(Msg.GetTodoListSuccess(todoList))

                                storage.save(
                                    TodoListCache(
                                        todoList = todoList,
                                    ),
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                is TodoListStore.Intent.CreateTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.createTodo(intent.todo)) {
                            is ApiResult.Success -> {
                                val todo = result.data.toDomain()

                                dispatch(Msg.CreateTodoSuccess(todo))

                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList + todo,
                                    ),
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                is TodoListStore.Intent.CompleteTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.completeTodo(intent.todoId)) {
                            is ApiResult.Success -> {
                                dispatch(Msg.CompleteTodoSuccess(intent.todoId))

                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList.filter { it.id != intent.todoId },
                                    ),
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                is TodoListStore.Intent.ModifyTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.modifyTodo(intent.todoId, intent.todo)) {
                            is ApiResult.Success -> {
                                val todo = result.data.toDomain()

                                dispatch(Msg.ModifyTodoSuccess(todo))

                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList.map { todo ->
                                            if (todo.id == todo.id) todo else todo
                                        },
                                    ),
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                is TodoListStore.Intent.DeleteTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.deleteTodo(intent.todoId)) {
                            is ApiResult.Success -> {
                                dispatch(Msg.DeleteTodoSuccess(intent.todoId))

                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList.filter { it.id != intent.todoId },
                                    ),
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<TodoListStore.State, Msg> {
        override fun TodoListStore.State.reduce(msg: Msg): TodoListStore.State {
            return when (msg) {
                Msg.Loading -> copy(
                    loading = true,
                    error = null,
                )

                Msg.Offline -> copy(
                    loading = false,
                    error = null,
                )

                is Msg.GetTodoListSuccess -> copy(
                    loading = false,
                    todoList = msg.todoList,
                    error = null,
                )

                is Msg.CreateTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList + msg.todo,
                    error = null,
                )

                is Msg.CompleteTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList.filter { it.id != msg.initId },
                    error = null,
                )

                is Msg.ModifyTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList.map { todo ->
                        if (todo.id == msg.todo.id) msg.todo else todo
                    },
                    error = null,
                )

                is Msg.DeleteTodoSuccess -> copy(
                    loading = false,
                    todoList = todoList.filter { it.id != msg.deleteId },
                    error = null,
                )

                is Msg.Error -> copy(
                    loading = false,
                    error = msg.message,
                )
            }
        }
    }
}
