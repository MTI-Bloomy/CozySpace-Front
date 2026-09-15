package bloomy.cozyspace.network

import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.data.dto.ChooseRewardRequestDto
import bloomy.cozyspace.data.dto.HouseDto
import bloomy.cozyspace.data.dto.LoginRequestDto
import bloomy.cozyspace.data.dto.RegisterRequestDto
import bloomy.cozyspace.data.dto.RegisterDto
import bloomy.cozyspace.data.dto.LoginDto
import bloomy.cozyspace.data.dto.RewardDto
import bloomy.cozyspace.data.dto.RoomDto
import bloomy.cozyspace.data.dto.TodoDto
import bloomy.cozyspace.data.dto.TodoRequestDto
import bloomy.cozyspace.data.dto.createHouseRequestDto
import bloomy.cozyspace.interfaces.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(
    private val client: HttpClient,
    private val networkMonitor: NetworkMonitor,
) {
    suspend fun signup(request: RegisterRequestDto): ApiResult<RegisterDto> = safeApiCall(networkMonitor) {
            client.post("${Environment.API_URL}/sign-up") {
                skipAuth()
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }

    suspend fun signin(request: LoginRequestDto): ApiResult<LoginDto> = safeApiCall(networkMonitor) {
            client.post("${Environment.API_URL}/sign-in") {
                skipAuth()
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }

    suspend fun getRewards(): ApiResult<List<RewardDto>> = safeApiCall(networkMonitor) {
        client.get("${Environment.API_URL}/reward")
    }

    suspend fun getReward(rewardId: String): ApiResult<RewardDto> = safeApiCall(networkMonitor) {
        client.get("${Environment.API_URL}/reward/$rewardId")
    }

    suspend fun chooseReward(request: ChooseRewardRequestDto) : ApiResult<RewardDto> = safeApiCall(networkMonitor) {
        client.put("${Environment.API_URL}/reward/choose") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun getRooms(houseId: String): ApiResult<List<RoomDto>> = safeApiCall(networkMonitor) {
        client.get("${Environment.API_URL}/room?houseId=$houseId")
    }

    suspend fun getHouse(): ApiResult<List<HouseDto>> = safeApiCall(networkMonitor) {
        client.get("${Environment.API_URL}/house")
    }
    
    suspend fun createHouse(request: createHouseRequestDto): ApiResult<HouseDto> = safeApiCall(networkMonitor) {
        client.post("${Environment.API_URL}/house") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun saveHouse(houseId: String): ApiResult<HouseDto> = safeApiCall(networkMonitor) {
        client.put("${Environment.API_URL}/house/${houseId}/save")
    }

    suspend fun getTodoList(): ApiResult<List<TodoDto>> = safeApiCall(networkMonitor) {
        client.get("${Environment.API_URL}/todoList")
    }

    suspend fun createTodo(request: TodoRequestDto): ApiResult<TodoDto> = safeApiCall(networkMonitor) {
        client.post("${Environment.API_URL}/todoList") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun completeTodo(id: String): ApiResult<TodoDto> = safeApiCall(networkMonitor) {
        client.put("${Environment.API_URL}/todoList/$id/complete")
    }

    suspend fun modifyTodo(id: String, request: TodoRequestDto): ApiResult<TodoDto> = safeApiCall(networkMonitor) {
        client.put("${Environment.API_URL}/todoList/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun deleteTodo(id: String): ApiResult<TodoDto> = safeApiCall(networkMonitor) {
        client.delete("${Environment.API_URL}/todoList/$id")
    }

    suspend fun getTodoDone(): ApiResult<List<TodoDto>> = safeApiCall(networkMonitor) {
        client.get("${Environment.API_URL}/todoDone")
    }
}
