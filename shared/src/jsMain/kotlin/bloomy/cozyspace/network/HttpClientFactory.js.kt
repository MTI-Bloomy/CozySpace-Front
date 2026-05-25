package bloomy.cozyspace.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

actual fun platformHttpClient(): HttpClient {
    return HttpClient(Js)
}
