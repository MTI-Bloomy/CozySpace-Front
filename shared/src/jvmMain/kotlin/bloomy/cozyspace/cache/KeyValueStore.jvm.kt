package bloomy.cozyspace.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

actual fun createKeyValueStore(): KeyValueStore = DesktopKeyValueStore()

class DesktopKeyValueStore(
    private val file: File = File("app_cache.json")
) : KeyValueStore {

    private val mutex = Mutex()

    private fun readAll(): MutableMap<String, String> {
        if (!file.exists()) return mutableMapOf()
        return Json.decodeFromString(file.readText())
    }

    private fun writeAll(map: Map<String, String>) {
        file.writeText(Json.encodeToString(map))
    }

    override suspend fun putString(key: String, value: String) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val map = readAll()
            map[key] = value
            writeAll(map)
        }
    }

    override suspend fun getString(key: String): String? = withContext(Dispatchers.IO) {
        mutex.withLock { readAll()[key] }
    }

    override suspend fun remove(key: String) = withContext(Dispatchers.IO) {
        mutex.withLock {
            val map = readAll()
            map.remove(key)
            writeAll(map)
        }
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        mutex.withLock { file.delete(); Unit }
    }
}
