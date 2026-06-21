package bloomy.cozyspace.network

import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.data.JokeDto
import bloomy.cozyspace.data.LoginRequestDto
import bloomy.cozyspace.data.RegisterRequestDto
import bloomy.cozyspace.data.RegisterDto
import bloomy.cozyspace.data.LoginDto
import bloomy.cozyspace.interfaces.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(
    private val client: HttpClient
) {
    suspend fun getRandomJoke(): JokeDto {
        return getJoke("/random")
    }

    suspend fun getRandomJokeByType(type: String): JokeDto {
        return getJoke("/type/$type/random")
    }

    private suspend fun getJoke(path: String): JokeDto {
        return client
            .get("${Environment.API_URL}$path") {
                header(HttpHeaders.Authorization, "Bearer ${Environment.API_TOKEN}")
            }
            .body()
    }

    suspend fun signup(
        request: RegisterRequestDto
    ): ApiResult<RegisterDto> =
        safeApiCall {
            client.post("${Environment.API_URL}/sign-up") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }

    suspend fun signin(
        request: LoginRequestDto
    ): ApiResult<LoginDto> =
        safeApiCall {
            client.post("${Environment.API_URL}/sign-in") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
}
