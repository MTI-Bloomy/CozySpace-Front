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
import bloomy.cozyspace.data.HouseRepository
import bloomy.cozyspace.domain.AiOptions
import bloomy.cozyspace.domain.House
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
class HouseJs(
    val id: String,
    val name: String,
    val rooms: Array<String>,
    val saveDate: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class HouseStateJs(
    val loading: Boolean,
    val house: HouseJs?,
    val savedHouses: Array<HouseJs>,
    val error: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class HouseLabelJs(
    val type: String,
    val message: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class HouseStoreJs internal constructor(
    private val store: HouseStore,
) {
    fun getHouse() {
        store.accept(HouseStore.Intent.GetHouse)
    }

    fun createHouse(name: String) {
        store.accept(HouseStore.Intent.CreateHouse(name))
    }

    fun saveHouse(houseId: String) {
        store.accept(HouseStore.Intent.SaveHouse(houseId))
    }

    fun clear() {
        store.accept(HouseStore.Intent.Clear)
    }

    fun subscribeState(onState: (HouseStateJs) -> Unit): () -> Unit {
        val disposable = store.states(observer { onState(it.toJs()) })
        return { disposable.dispose() }
    }

    fun subscribeLabels(onLabel: (HouseLabelJs) -> Unit): () -> Unit {
        val disposable = store.labels(observer { onLabel(it.toJs()) })
        return { disposable.dispose() }
    }
}

private fun House.toJs() = HouseJs(
    id = id,
    name = name,
    rooms = rooms.toTypedArray(),
    saveDate = saveDate?.toString(),
)

private fun HouseStore.State.toJs() = HouseStateJs(
    loading = loading,
    house = house?.toJs(),
    savedHouses = savedHouses.map { it.toJs() }.toTypedArray(),
    error = error,
)

private fun HouseStore.Label.toJs() = when (this) {
    is HouseStore.Label.ShowError -> HouseLabelJs("ERROR", message)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun createHouseStore(): Promise<HouseStoreJs> {
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
        HouseStoreFactory(
            repository = HouseRepository(ApiService(httpClient, networkMonitor)),
            storage = storages.houseStorage,
        ).create().also { it.init() }.let(::HouseStoreJs)
    }
}
