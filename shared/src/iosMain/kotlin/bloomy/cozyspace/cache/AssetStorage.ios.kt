package bloomy.cozyspace.cache

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.writeToFile
import platform.posix.memcpy

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

    override fun path(key: String): Path = "$dirPath/$key".toPath()
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
