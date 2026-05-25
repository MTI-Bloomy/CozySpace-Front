package bloomy.cozyspace.network

import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.data.JokeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

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
}
