package bloomy.cozyspace.cache

import kotlinx.browser.localStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

actual fun createUserStorage(): UserStorage =
    JsUserStorage()

class JsUserStorage : UserStorage {

    override suspend fun save(cache: UserCache) {
        localStorage.setItem(
            "user_cache",
            Json.encodeToString(cache)
        )
    }

    override suspend fun get(): UserCache? {
        val json = localStorage.getItem("user_cache")
            ?: return null

        return Json.decodeFromString(json)
    }

    override suspend fun clear() {
        localStorage.removeItem("user_cache")
    }
}
