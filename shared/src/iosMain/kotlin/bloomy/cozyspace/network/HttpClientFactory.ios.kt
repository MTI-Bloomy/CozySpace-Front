package bloomy.cozyspace.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun platformHttpClient(): HttpClient {
    return HttpClient(Darwin)
}
