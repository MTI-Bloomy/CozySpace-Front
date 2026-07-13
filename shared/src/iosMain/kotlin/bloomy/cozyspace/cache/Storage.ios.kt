package bloomy.cozyspace.cache

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.posix.memcpy

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

actual fun createAssetStorage(): AssetStorage = IosAssetStorage()

class IosAssetStorage : AssetStorage {

    @OptIn(ExperimentalForeignApi::class)
    private val dirPath: String by lazy {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSApplicationSupportDirectory, NSUserDomainMask, true
        )
        val base = paths.first() as String
        val full = "$base/assets_cache"
        NSFileManager.defaultManager.createDirectoryAtPath(
            full, true, null, null
        )
        full
    }

    override suspend fun save(key: String, bytes: ByteArray) {
        val data = bytes.toNSData()
        data.writeToFile("$dirPath/$key", true)
    }

    override suspend fun get(key: String): ByteArray? {
        val path = "$dirPath/$key"
        if (!NSFileManager.defaultManager.fileExistsAtPath(path)) return null
        val data = NSData.dataWithContentsOfFile(path) ?: return null
        return data.toByteArray()
    }

    override suspend fun exists(key: String): Boolean =
        NSFileManager.defaultManager.fileExistsAtPath("$dirPath/$key")

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun clear() {
        NSFileManager.defaultManager.contentsOfDirectoryAtPath(dirPath, null)?.forEach {
            NSFileManager.defaultManager.removeItemAtPath("$dirPath/$it", null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray = ByteArray(length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), bytes, length)
    }
}
