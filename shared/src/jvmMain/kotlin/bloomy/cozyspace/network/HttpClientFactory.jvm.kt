package bloomy.cozyspace.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun platformHttpClient(): HttpClient {
    return HttpClient(CIO)
}
