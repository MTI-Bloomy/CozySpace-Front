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
}
