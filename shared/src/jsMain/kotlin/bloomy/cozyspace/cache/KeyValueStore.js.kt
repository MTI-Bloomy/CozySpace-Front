package bloomy.cozyspace.cache

import kotlinx.browser.localStorage

actual fun createKeyValueStore(): KeyValueStore = JsKeyValueStore()

class JsKeyValueStore : KeyValueStore {
    override suspend fun putString(key: String, value: String) {
        localStorage.setItem(key, value)
    }

    override suspend fun getString(key: String): String? = localStorage.getItem(key)

    override suspend fun remove(key: String) {
        localStorage.removeItem(key)
    }

    override suspend fun clear() {
        localStorage.clear() // efface tout le localStorage de l'origine — à utiliser à bon escient
    }
}
