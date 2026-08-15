package bloomy.cozyspace.cache

import okio.Path

interface AssetStorage {
    suspend fun save(key: String, bytes: ByteArray)
    suspend fun get(key: String): ByteArray?
    suspend fun exists(key: String): Boolean
    suspend fun clear()
    fun path(key: String): Path
}

expect fun createAssetStorage(): AssetStorage
