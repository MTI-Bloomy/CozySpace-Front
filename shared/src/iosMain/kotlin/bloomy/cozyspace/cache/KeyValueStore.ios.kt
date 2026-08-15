package bloomy.cozyspace.cache

import platform.Foundation.NSUserDefaults
import platform.Foundation.*

actual fun createKeyValueStore(): KeyValueStore = IOSKeyValueStore()

class IOSKeyValueStore : KeyValueStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    override suspend fun putString(key: String, value: String) {
        defaults.setObject(value, key)
    }

    override suspend fun getString(key: String): String? = defaults.stringForKey(key)

    override suspend fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }

    override suspend fun clear() {
        val domain = NSBundle.mainBundle.bundleIdentifier
        if (domain != null) defaults.removePersistentDomainForName(domain)
    }
}
