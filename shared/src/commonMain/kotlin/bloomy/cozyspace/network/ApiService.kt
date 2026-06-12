package bloomy.cozyspace.network

import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.data.JokeDto
import bloomy.cozyspace.data.LoginRequestDto
import bloomy.cozyspace.data.RegisterRequestDto
import bloomy.cozyspace.data.RegisterDto
import bloomy.cozyspace.data.LoginDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.client.request.setBody

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

    suspend fun signup(request: RegisterRequestDto): RegisterDto {
        return client
            .post("${Environment.API_URL}/sign-up") {
                header(HttpHeaders.ContentType, "application/json")
                setBody(request)
            }
            .body()
    }

    suspend fun signin(request: LoginRequestDto): LoginDto {
        return client
            .post("${Environment.API_URL}/sign-in") {
                header(HttpHeaders.ContentType, "application/json")
                setBody(request)
            }
            .body()
    }
}
