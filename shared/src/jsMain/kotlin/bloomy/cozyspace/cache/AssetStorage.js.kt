package bloomy.cozyspace.cache

import kotlinx.coroutines.suspendCancellableCoroutine
import okio.Path
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

external val indexedDB: dynamic

actual fun createAssetStorage(): AssetStorage = JsAssetStorage()

class JsAssetStorage : AssetStorage {

    private val dbName = "assets_cache_db"
    private val storeName = "assets"
    private var dbInstance: dynamic = null

    private suspend fun db(): dynamic {
        if (dbInstance == null) dbInstance = openDb()
        return dbInstance
    }

    private suspend fun openDb(): dynamic = suspendCancellableCoroutine { cont ->
        val request = indexedDB.open(dbName, 1)
        request.onupgradeneeded = { event: dynamic ->
            event.target.result.createObjectStore(storeName)
            Unit
        }
        request.onsuccess = { event: dynamic -> cont.resume(event.target.result); Unit }
        request.onerror = { _: dynamic -> cont.resumeWithException(Exception("Ouverture IndexedDB échouée")); Unit }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun save(key: String, bytes: ByteArray) {
        val database = db()
        val base64 = Base64.encode(bytes)
        suspendCancellableCoroutine<Unit> { cont ->
            val store = database.transaction(arrayOf(storeName), "readwrite").objectStore(storeName)
            val request = store.put(base64, key)
            request.onsuccess = { _: dynamic -> cont.resume(Unit); Unit }
            request.onerror = { _: dynamic -> cont.resumeWithException(Exception("Sauvegarde asset échouée")); Unit }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun get(key: String): ByteArray? {
        val database = db()
        val base64: String? = suspendCancellableCoroutine { cont ->
            val store = database.transaction(arrayOf(storeName), "readonly").objectStore(storeName)
            val request = store.get(key)
            request.onsuccess = { event: dynamic ->
                val result = event.target.result
                cont.resume(if (result == null || result == undefined) null else result as String)
                Unit
            }
            request.onerror = { _: dynamic -> cont.resumeWithException(Exception("Lecture asset échouée")); Unit }
        }
        return base64?.let { Base64.decode(it) }
    }

    override suspend fun exists(key: String): Boolean = get(key) != null

    override suspend fun clear() {
        val database = db()
        suspendCancellableCoroutine<Unit> { cont ->
            val request = database.transaction(arrayOf(storeName), "readwrite").objectStore(storeName).clear()
            request.onsuccess = { _: dynamic -> cont.resume(Unit); Unit }
            request.onerror = { _: dynamic -> cont.resumeWithException(Exception("Clear échoué")); Unit }
        }
    }

    override fun path(key: String): Path {
        TODO("Not yet implemented. Note: it seems that it's not possible to return a path")
    }
}
