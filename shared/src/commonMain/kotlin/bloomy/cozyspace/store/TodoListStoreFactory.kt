package bloomy.cozyspace.store

import bloomy.cozyspace.cache.PendingAction
import bloomy.cozyspace.cache.SyncQueue
import bloomy.cozyspace.cache.TodoListCache
import bloomy.cozyspace.cache.TodoListStorage
import bloomy.cozyspace.data.TodoListRepository
import bloomy.cozyspace.data.dto.TodoDto
import bloomy.cozyspace.data.dto.TodoRequestDto
import bloomy.cozyspace.data.dto.toDomain
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.NetworkAwareBootstrapper
import bloomy.cozyspace.network.NetworkAwareExecutor
import bloomy.cozyspace.network.NetworkMonitor
import bloomy.cozyspace.utils.parseRoomType
import bloomy.cozyspace.utils.randomId
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch
import kotlin.time.Clock

class TodoListStoreFactory(
    private val repository: TodoListRepository,
    private val storage: TodoListStorage,
    private val networkMonitor: NetworkMonitor,
    private val syncQueue: SyncQueue,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): TodoListStore {
        val cache = storage.get()

        val initialState = TodoListStore.State(
            todoList = cache?.todoList ?: emptyList(),
        )

        return object : TodoListStore,
            Store<TodoListStore.Intent, TodoListStore.State, TodoListStore.Label> by storeFactory.create(
                initialState = initialState,
                bootstrapper = NetworkAwareBootstrapper(),
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
        data object Clear : Msg
    }

    private inner class ExecutorImpl : NetworkAwareExecutor<
        TodoListStore.Intent,
        TodoListStore.State,
        Msg,
        TodoListStore.Label,
        >(networkMonitor) {

        override fun executeIntent(intent: TodoListStore.Intent) {
            when (intent) {
                is TodoListStore.Intent.GetTodoList -> {
                    dispatch(Msg.Loading)
                    scope.launch { getTodos() }
                }

                is TodoListStore.Intent.CreateTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.createTodo(intent.todo)) {
                            is ApiResult.Success -> {
                                val newTodo = result.data.toDomain()

                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList + newTodo,
                                    ),
                                )

                                dispatch(Msg.CreateTodoSuccess(newTodo))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> createOffline(intent.todo)
                        }
                    }
                }

                is TodoListStore.Intent.CompleteTodo -> {
                    dispatch(Msg.Loading)
                    val completedTodo = state().todoList.find { it.id == intent.todoId }

                    scope.launch {
                        when (val result = repository.completeTodo(intent.todoId)) {
                            is ApiResult.Success -> {
                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList.filter { it.id != intent.todoId },
                                    ),
                                )

                                dispatch(Msg.CompleteTodoSuccess(intent.todoId))

                                completedTodo?.let { publish(TodoListStore.Label.TodoCompleted(it)) }
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> completeOffline(intent.todoId, completedTodo)
                        }
                    }
                }

                is TodoListStore.Intent.ModifyTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.modifyTodo(intent.todoId, intent.todo)) {
                            is ApiResult.Success -> {
                                val editedTodo = result.data.toDomain()

                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList.map { todo ->
                                            if (todo.id == editedTodo.id) editedTodo else todo
                                        },
                                    ),
                                )

                                dispatch(Msg.ModifyTodoSuccess(editedTodo))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> modifyOffline(intent.todoId, intent.todo)
                        }
                    }
                }

                is TodoListStore.Intent.DeleteTodo -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.deleteTodo(intent.todoId)) {
                            is ApiResult.Success -> {
                                storage.save(
                                    TodoListCache(
                                        todoList = state().todoList.filter { it.id != intent.todoId },
                                    ),
                                )

                                dispatch(Msg.DeleteTodoSuccess(intent.todoId))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TodoListStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> deleteOffline(intent.todoId)
                        }
                    }
                }

                TodoListStore.Intent.Clear -> dispatch(Msg.Clear)
            }
        }

        private suspend fun getTodos() {
            when (val result = repository.getTodoList()) {
                is ApiResult.Success -> {
                    val todoList = result.data.map { it.toDomain() }

                    storage.save(
                        TodoListCache(
                            todoList = todoList,
                        ),
                    )

                    dispatch(Msg.GetTodoListSuccess(todoList))
                }

                is ApiResult.Error -> {
                    dispatch(Msg.Error(result.message))
                    publish(TodoListStore.Label.ShowError(result.message))
                }

                ApiResult.Offline -> dispatch(Msg.Offline)
            }
        }

        // --- Branches offline : état optimiste local + mise en queue ---

        private suspend fun createOffline(request: TodoRequestDto) {
            val localId = randomId() // même id pour le Todo optimiste ET pour l'action, afin de les recoller au replay

            val newTodo = Todo(
                id = localId,
                name = request.name,
                type = parseRoomType(request.type),
                date = request.nextDueDate,
                frequency = request.frequency,
            )

            storage.save(
                TodoListCache(
                    todoList = state().todoList + newTodo,
                ),
            )

            dispatch(Msg.CreateTodoSuccess(newTodo))

            syncQueue.enqueue(
                PendingAction.CreateTodo(
                    id = localId,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    todo = request,
                ),
            )
        }

        private suspend fun completeOffline(todoId: String, completedTodo: Todo?) {
            storage.save(
                TodoListCache(
                    todoList = state().todoList.filter { it.id != todoId },
                ),
            )

            dispatch(Msg.CompleteTodoSuccess(todoId))

            completedTodo?.let { publish(TodoListStore.Label.TodoCompleted(it)) }

            syncQueue.enqueue(
                PendingAction.CompleteTodo(
                    id = randomId(),
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    todoId = todoId,
                ),
            )
        }

        private suspend fun modifyOffline(todoId: String, request: TodoRequestDto) {
            val editedTodo = state().todoList.find { it.id == todoId }?.copy(
                name = request.name,
                type = parseRoomType(request.type),
                date = request.nextDueDate,
                frequency = request.frequency,
            ) ?: return

            storage.save(
                TodoListCache(
                    todoList = state().todoList.map { todo ->
                        if (todo.id == editedTodo.id) editedTodo else todo
                    },
                ),
            )

            dispatch(Msg.ModifyTodoSuccess(editedTodo))

            syncQueue.enqueue(
                PendingAction.ModifyTodo(
                    id = randomId(),
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    todoId = todoId,
                    todo = request,
                ),
            )
        }

        private suspend fun deleteOffline(todoId: String) {
            storage.save(
                TodoListCache(
                    todoList = state().todoList.filter { it.id != todoId },
                ),
            )

            dispatch(Msg.DeleteTodoSuccess(todoId))

            syncQueue.enqueue(
                PendingAction.DeleteTodo(
                    id = randomId(),
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    todoId = todoId,
                ),
            )
        }

        // --- Replay à la reconnexion ---

        private fun targetKey(action: PendingAction): String = when (action) {
            is PendingAction.CreateTodo -> action.id // le todo n'existe pas encore : sa clé, c'est l'id de sa propre création
            is PendingAction.CompleteTodo -> action.todoId
            is PendingAction.ModifyTodo -> action.todoId
            is PendingAction.DeleteTodo -> action.todoId
        }

        override suspend fun onReconnect() {
            val grouped = syncQueue.pending.value
                .sortedBy { it.createdAt }
                .groupBy(::targetKey)

            if (grouped.isEmpty()) return

            for ((_, actions) in grouped) {
                // Créé puis supprimé hors ligne : jamais existé côté serveur, aucun appel à faire
                if (actions.any { it is PendingAction.CreateTodo } && actions.any { it is PendingAction.DeleteTodo }) {
                    actions.forEach { syncQueue.remove(it.id) }
                    continue
                }

                var realId: String? = null // rempli dès que le CreateTodo de ce groupe réussit

                for (action in actions) {
                    val result = when (action) {
                        is PendingAction.CreateTodo -> repository.createTodo(action.todo)
                        is PendingAction.CompleteTodo -> repository.completeTodo(realId ?: action.todoId)
                        is PendingAction.ModifyTodo -> repository.modifyTodo(realId ?: action.todoId, action.todo)
                        is PendingAction.DeleteTodo -> repository.deleteTodo(realId ?: action.todoId)
                    }

                    when (result) {
                        is ApiResult.Success<*> -> {
                            syncQueue.remove(action.id)
                            if (action is PendingAction.CreateTodo) {
                                realId =
                                    (result.data as TodoDto).id // les actions suivantes DU MEME groupe visent désormais le vrai id
                            }
                        }

                        else -> break // stoppe CE groupe seulement ; les autres todos continuent d'être rejoués
                    }
                }
            }

            getTodos() // le serveur redevient la source de vérité pour toute la liste, ids temporaires inclus
            publish(TodoListStore.Label.RefreshTodoDone)
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

                Msg.Clear -> TodoListStore.State()
            }
        }
    }
}
