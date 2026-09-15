package bloomy.cozyspace.cache

import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual fun createAssetStorage(): AssetStorage = DesktopAssetStorage()

class DesktopAssetStorage : AssetStorage {

    private val dir = File("assets_cache").apply { mkdirs() }

    override suspend fun save(key: String, bytes: ByteArray) {
        val file = File(dir, key)
        file.parentFile?.mkdirs() // <- crée kitchen/floor/ si besoin
        file.writeBytes(bytes)
    }

    override suspend fun get(key: String): ByteArray? {
        val file = File(dir, key)
        return if (file.exists()) file.readBytes() else null
    }

    override suspend fun exists(key: String): Boolean = File(dir, key).exists()

    override suspend fun clear() {
        dir.listFiles()?.forEach { it.delete() }
    }

    override fun path(key: String): Path = File(dir, key).absolutePath.toPath()
}
