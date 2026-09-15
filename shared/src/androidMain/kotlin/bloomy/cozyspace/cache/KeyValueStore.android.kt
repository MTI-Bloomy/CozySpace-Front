package bloomy.cozyspace.cache

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first

@SuppressLint("StaticFieldLeak")
object AppContextProvider {
    lateinit var context: Context
        private set

    fun init(context: Context) {
        this.context = context.applicationContext
    }
}

actual fun createKeyValueStore(): KeyValueStore = AndroidKeyValueStore(AppContextProvider.context)

class AndroidKeyValueStore(
    private val context: Context
) : KeyValueStore {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("app_cache") }
    )

    override suspend fun putString(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    override suspend fun getString(key: String): String? =
        dataStore.data.first()[stringPreferencesKey(key)]

    override suspend fun remove(key: String) {
        dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
