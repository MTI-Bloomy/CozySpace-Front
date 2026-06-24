package bloomy.cozyspace.cache

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IOSUserStorage : UserStorage {

    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun save(cache: UserCache) {
        val json = Json.encodeToString(cache)
        defaults.setObject(json, "user_cache")
    }

    override suspend fun get(): UserCache? {
        val json = defaults.stringForKey("user_cache") ?: return null
        return Json.decodeFromString(json)
    }

    override suspend fun clear() {
        defaults.removeObjectForKey("user_cache")
    }
}

actual fun createUserStorage(): UserStorage =
    IOSUserStorage()
