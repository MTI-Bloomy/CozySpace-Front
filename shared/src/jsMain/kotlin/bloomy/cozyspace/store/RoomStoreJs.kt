package bloomy.cozyspace.store

import bloomy.cozyspace.cache.HouseStorage
import bloomy.cozyspace.cache.RewardStorage
import bloomy.cozyspace.cache.RoomStorage
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.cache.SyncQueueStorage
import bloomy.cozyspace.cache.TimerMusicStorage
import bloomy.cozyspace.cache.TodoDoneStorage
import bloomy.cozyspace.cache.TodoListStorage
import bloomy.cozyspace.cache.UserCache
import bloomy.cozyspace.cache.UserStorage
import bloomy.cozyspace.cache.createAssetStorage
import bloomy.cozyspace.cache.createKeyValueStore
import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.data.RoomRepository
import bloomy.cozyspace.domain.AiOptions
import bloomy.cozyspace.domain.Room
import bloomy.cozyspace.domain.User
import bloomy.cozyspace.network.ApiService
import bloomy.cozyspace.network.NetworkMonitor
import bloomy.cozyspace.network.createHttpClient
import com.arkivanov.mvikotlin.core.rx.observer
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.Promise

@OptIn(ExperimentalJsExport::class)
@JsExport
class RoomJs(
    val id: String,
    val maxCapacity: Int,
    val type: String,               // "GARDEN", "WORK", "BEDROOM"...
    val furniture: Array<String>,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class RoomStateJs(
    val loading: Boolean,
    val rooms: Array<RoomJs>,
    val error: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class RoomLabelJs(
    val type: String,
    val message: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class RoomStoreJs internal constructor(
    private val store: RoomStore,
) {
    fun getRooms(houseId: String, saveMode: Boolean = false) {
        store.accept(RoomStore.Intent.GetRooms(houseId, saveMode))
    }

    fun clear() {
        store.accept(RoomStore.Intent.Clear)
    }

    fun subscribeState(onState: (RoomStateJs) -> Unit): () -> Unit {
        val disposable = store.states(observer { onState(it.toJs()) })
        return { disposable.dispose() }
    }

    fun subscribeLabels(onLabel: (RoomLabelJs) -> Unit): () -> Unit {
        val disposable = store.labels(observer { onLabel(it.toJs()) })
        return { disposable.dispose() }
    }
}

private fun Room.toJs() = RoomJs(
    id = id,
    maxCapacity = maxCapacity,
    type = type.name,
    furniture = furniture.toTypedArray(),
)

private fun RoomStore.State.toJs() = RoomStateJs(
    loading = loading,
    rooms = rooms.map { it.toJs() }.toTypedArray(),
    error = error,
)

private fun RoomStore.Label.toJs() = when (this) {
    is RoomStore.Label.ShowError -> RoomLabelJs("ERROR", message)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun createRoomStore(): Promise<RoomStoreJs> {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    return scope.promise {
        val keyValueStore = createKeyValueStore()
        val storages = Storages(
            assetStorage = createAssetStorage(),
            keyValueStore = keyValueStore,
            userStorage = UserStorage(keyValueStore),
            houseStorage = HouseStorage(keyValueStore),
            roomStorage = RoomStorage(keyValueStore),
            rewardStorage = RewardStorage(keyValueStore),
            todoListStorage = TodoListStorage(keyValueStore),
            todoDoneStorage = TodoDoneStorage(keyValueStore),
            timerMusicStorage = TimerMusicStorage(keyValueStore),
            syncQueueStorage = SyncQueueStorage(keyValueStore),
        )
        val httpClient = createHttpClient(
            baseUrl = Environment.API_URL,
            loadTokens = {
                storages.userStorage.get()?.token
                    ?.takeIf { it.idToken.isNotBlank() }
                    ?.let { BearerTokens(it.idToken, it.refreshToken) }
            },
            onTokensRefreshed = { token ->
                val cache = storages.userStorage.get()
                storages.userStorage.save(
                    UserCache(
                        token = token,
                        user = cache?.user ?: User(
                            "", "", "", null, null, null, null,
                            AiOptions(aiCheer = false, aiJournalPrompts = false, aiTodo = false),
                        ),
                    ),
                )
            },
            onRefreshFailed = {
                storages.userStorage.clear()
            },
        )
        val networkMonitor = NetworkMonitor(httpClient, scope)
        RoomStoreFactory(
            repository = RoomRepository(ApiService(httpClient, networkMonitor)),
            storage = storages.roomStorage,
        ).create().also { it.init() }.let(::RoomStoreJs)
    }
}
