package bloomy.cozyspace.network

import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.data.dto.LoginRequestDto
import bloomy.cozyspace.data.dto.RegisterRequestDto
import bloomy.cozyspace.data.dto.RegisterDto
import bloomy.cozyspace.data.dto.LoginDto
import bloomy.cozyspace.interfaces.ApiResult
import io.ktor.client.HttpClient
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
//    Headers Example
//    private suspend fun get(path: String): Dto {
//        return client
//            .get("${Environment.API_URL}$path") {
//                header(HttpHeaders.Authorization, "Bearer ${Environment.API_TOKEN}")
//            }
//            .body()
//    }

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
