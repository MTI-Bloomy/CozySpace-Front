package bloomy.cozyspace.cache

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@SuppressLint("StaticFieldLeak")
object AppContextProvider {
    lateinit var context: Context
        private set

    fun init(context: Context) {
        this.context = context.applicationContext
    }
}

actual fun createUserStorage(): UserStorage = AndroidUserStorage(AppContextProvider.context)

class AndroidUserStorage(
    private val context: Context
) : UserStorage {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("user_cache")
        }
    )

    override suspend fun save(cache: UserCache) {
        val json = Json.encodeToString(cache)

        dataStore.edit {
            it[stringPreferencesKey("cache")] = json
        }
    }

    override suspend fun get(): UserCache? {
        val prefs = dataStore.data.first()
        val json = prefs[stringPreferencesKey("cache")] ?: return null

        return Json.decodeFromString(json)
    }

    override suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}

actual fun createAssetStorage(): AssetStorage = AndroidAssetStorage(AppContextProvider.context)

class AndroidAssetStorage(
    private val context: Context
) : AssetStorage {

    private val dir = File(context.filesDir, "assets_cache").apply { mkdirs() }

    override suspend fun save(key: String, bytes: ByteArray) = withContext(Dispatchers.IO) {
        File(dir, key).writeBytes(bytes)
    }

    override suspend fun get(key: String): ByteArray? = withContext(Dispatchers.IO) {
        val file = File(dir, key)
        if (file.exists()) file.readBytes() else null
    }

    override suspend fun exists(key: String): Boolean = File(dir, key).exists()

    override suspend fun clear() {
        dir.listFiles()?.forEach { it.delete() }
    }
}
