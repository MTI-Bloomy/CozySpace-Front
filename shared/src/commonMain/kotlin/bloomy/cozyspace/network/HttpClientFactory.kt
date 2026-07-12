package bloomy.cozyspace.network

import bloomy.cozyspace.data.dto.RefreshDto
import bloomy.cozyspace.data.dto.RefreshRequestDto
import bloomy.cozyspace.domain.Token
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json

expect fun platformHttpClient(): HttpClient

val SkipAuthAttributeKey = AttributeKey<Boolean>("SkipAuth")

fun HttpRequestBuilder.skipAuth() {
    attributes.put(SkipAuthAttributeKey, true)
}

suspend fun HttpClient.clearBearerCache() {
    plugin(Auth).providers
        .filterIsInstance<BearerAuthProvider>()
        .firstOrNull()
        ?.clearToken()
}

fun createHttpClient(
    baseUrl: String,
    loadTokens: suspend () -> BearerTokens?,
    onTokensRefreshed: suspend (Token) -> Unit,
    onRefreshFailed: suspend () -> Unit
): HttpClient {
    return platformHttpClient().config {
        expectSuccess = false

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
        }

        install(Auth) {
            bearer {
                loadTokens {
                    loadTokens()
                }

                refreshTokens {
                    val refreshToken = oldTokens?.refreshToken

                    if (refreshToken.isNullOrBlank()) {
                        onRefreshFailed()
                        return@refreshTokens null
                    }

                    val response = client.post("$baseUrl/refresh") {
                        markAsRefreshTokenRequest() // évite la boucle infinie
                        contentType(ContentType.Application.Json)
                        setBody(
                            RefreshRequestDto(refresh_token = refreshToken)
                        )
                    }

                    val bodyText = response.bodyAsText()

                    if (response.status.isSuccess()) {
                        val dto = Json.decodeFromString<RefreshDto>(bodyText)
                        val newToken = Token(
                            idToken = dto.idToken,
                            refreshToken = dto.refreshToken ?: refreshToken
                        )

                        onTokensRefreshed(newToken)

                        BearerTokens(newToken.idToken, newToken.refreshToken)
                    } else {
                        onRefreshFailed()
                        null
                    }
                }

                sendWithoutRequest { request ->
                    request.attributes.getOrNull(SkipAuthAttributeKey) != true
                }
            }
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }
}
