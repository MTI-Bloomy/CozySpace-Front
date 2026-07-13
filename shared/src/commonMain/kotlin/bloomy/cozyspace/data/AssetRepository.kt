package bloomy.cozyspace.data

import bloomy.cozyspace.cache.AssetStorage
import bloomy.cozyspace.network.skipAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class AssetRepository(
    private val httpClient: HttpClient,
    private val assetStorage: AssetStorage
) {
    suspend fun getAsset(key: String, presignedUrl: String): ByteArray {
        assetStorage.get(key)?.let { return it }

        val response = httpClient.get(presignedUrl) {
            skipAuth()
        }
        if (!response.status.isSuccess()) {
            throw RuntimeException("Échec téléchargement asset: ${response.status}") as Throwable
        }

        val bytes = response.body<ByteArray>()
        assetStorage.save(key, bytes)
        return bytes
    }
}
