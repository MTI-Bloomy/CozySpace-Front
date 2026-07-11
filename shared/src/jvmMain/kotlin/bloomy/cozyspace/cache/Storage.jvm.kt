package bloomy.cozyspace.cache

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

actual fun createUserStorage(): UserStorage =
    DesktopUserStorage()

class DesktopUserStorage : UserStorage {

    private val file = File("user_cache.json")

    override suspend fun save(cache: UserCache) {
        file.writeText(Json.encodeToString(cache))
    }

    override suspend fun get(): UserCache? {
        if (!file.exists()) return null
        return Json.decodeFromString(file.readText())
    }

    override suspend fun clear() {
        file.delete()
    }
}
