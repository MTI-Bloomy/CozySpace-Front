package bloomy.cozyspace.network

import bloomy.cozyspace.data.dto.ErrorDto
import bloomy.cozyspace.interfaces.ApiResult
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

suspend inline fun <reified T> safeApiCall(
    crossinline block: suspend () -> HttpResponse
): ApiResult<T> {
    return try {
        val response = block()

        when {
            response.status.isSuccess() -> {
                val body = response.bodyAsText()

                if (body.isBlank()) {
                    ApiResult.Empty
                } else {
                    ApiResult.Success(
                        AppJson.decodeFromString<T>(body)
                    )
                }
            }

            else -> {
                val errorBody = response.bodyAsText()

                val message = runCatching {
                    AppJson.decodeFromString<ErrorDto>(errorBody).message
                }.getOrElse {
                    errorBody.ifBlank { "Unknown server error" }
                }

                ApiResult.Error(
                    code = response.status.value,
                    message = message
                )
            }
        }

    } catch (e: Exception) {
        ApiResult.Error(
            message = e.message ?: "Unknown error"
        )
    }
}
