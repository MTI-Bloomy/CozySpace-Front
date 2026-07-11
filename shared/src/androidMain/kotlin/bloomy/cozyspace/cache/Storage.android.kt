package bloomy.cozyspace.cache

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
