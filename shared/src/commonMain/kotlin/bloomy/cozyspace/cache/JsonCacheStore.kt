package bloomy.cozyspace.cache

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class JsonCacheStore<T>(
    private val store: KeyValueStore,
    private val key: String,
    private val serializer: KSerializer<T>,
    private val json: Json = Json,
) {
    suspend fun save(value: T) {
        store.putString(key, json.encodeToString(serializer, value))
    }

    suspend fun get(): T? =
        store.getString(key)?.let { json.decodeFromString(serializer, it) }

    suspend fun clear() {
        store.remove(key) // ne touche que CETTE clé, pas tout le store
    }
}
