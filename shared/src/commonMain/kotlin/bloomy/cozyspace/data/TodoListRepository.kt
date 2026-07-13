package bloomy.cozyspace.data

import bloomy.cozyspace.data.dto.TodoDto
import bloomy.cozyspace.data.dto.TodoRequestDto
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class TodoListRepository(
    private val api: ApiService
) {
    suspend fun getTodoList(): ApiResult<List<TodoDto>> {
        return api.getTodoList()
    }

    suspend fun createTodo(todo: TodoRequestDto): ApiResult<TodoDto> {
        return api.createTodo(todo)
    }

    suspend fun completeTodo(todoId: String): ApiResult<TodoDto> {
        return api.completeTodo(todoId)
    }
}
