package bloomy.cozyspace.cache

interface KeyValueStore {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
    suspend fun remove(key: String)
    suspend fun clear()
}

expect fun createKeyValueStore(): KeyValueStore
