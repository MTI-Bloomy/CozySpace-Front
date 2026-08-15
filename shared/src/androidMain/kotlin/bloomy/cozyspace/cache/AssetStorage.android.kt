package bloomy.cozyspace.cache

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

actual fun createAssetStorage(): AssetStorage = AndroidAssetStorage(AppContextProvider.context)

class AndroidAssetStorage(
    private val context: Context
) : AssetStorage {

    private val dir = File(context.filesDir, "assets_cache").apply { mkdirs() }

    override suspend fun save(key: String, bytes: ByteArray) = withContext(Dispatchers.IO) {
        val file = File(dir, key)
        file.parentFile?.mkdirs()
        file.writeBytes(bytes)
    }

    override suspend fun get(key: String): ByteArray? = withContext(Dispatchers.IO) {
        val file = File(dir, key)
        if (file.exists()) file.readBytes() else null
    }

    override suspend fun exists(key: String): Boolean = File(dir, key).exists()

    override suspend fun clear() {
        dir.listFiles()?.forEach { it.delete() }
    }

    override fun path(key: String): Path = File(dir, key).absolutePath.toPath()
}
