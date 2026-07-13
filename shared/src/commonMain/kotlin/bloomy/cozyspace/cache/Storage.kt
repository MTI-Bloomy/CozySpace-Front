package bloomy.cozyspace.cache

interface UserStorage {
    suspend fun save(cache: UserCache)
    suspend fun get(): UserCache?
    suspend fun clear()
}

expect fun createUserStorage(): UserStorage

interface AssetStorage {
    suspend fun save(key: String, bytes: ByteArray)
    suspend fun get(key: String): ByteArray?
    suspend fun exists(key: String): Boolean
    suspend fun clear()
}

expect fun createAssetStorage(): AssetStorage
