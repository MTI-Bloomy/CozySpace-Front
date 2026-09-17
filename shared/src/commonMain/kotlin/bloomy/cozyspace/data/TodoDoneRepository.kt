package bloomy.cozyspace.data

import bloomy.cozyspace.data.dto.TodoDto
import bloomy.cozyspace.interfaces.ApiResult
import bloomy.cozyspace.network.ApiService

class TodoDoneRepository(
    private val api: ApiService
) {
    suspend fun getTodoDone(): ApiResult<List<TodoDto>> {
        return api.getTodoDone()
    }
}
