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
import bloomy.cozyspace.data.AssetRepository
import bloomy.cozyspace.data.RewardRepository
import bloomy.cozyspace.data.dto.ChooseRewardRequestDto
import bloomy.cozyspace.domain.AiOptions
import bloomy.cozyspace.domain.Reward
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
class RewardJs(
    val id: String,
    val furnitureId: String,
    val furnitureLink: String,
    val todoDoneId: String,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class RewardStateJs(
    val loading: Boolean,
    val rewards: Array<RewardJs>,
    val error: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class RewardLabelJs(
    val type: String,
    val message: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class RewardStoreJs internal constructor(
    private val store: RewardStore,
) {
    fun getRewards() {
        store.accept(RewardStore.Intent.GetRewards)
    }

    fun getReward(id: String) {
        store.accept(RewardStore.Intent.GetReward(id))
    }

    fun chooseReward(todoDoneId: String, houseId: String, roomId: String, placement: String) {
        store.accept(
            RewardStore.Intent.ChooseReward(
                ChooseRewardRequestDto(todoDoneId, houseId, roomId, placement),
            ),
        )
    }

    fun clear() {
        store.accept(RewardStore.Intent.Clear)
    }

    fun subscribeState(onState: (RewardStateJs) -> Unit): () -> Unit {
        val disposable = store.states(observer { onState(it.toJs()) })
        return { disposable.dispose() }
    }

    fun subscribeLabels(onLabel: (RewardLabelJs) -> Unit): () -> Unit {
        val disposable = store.labels(observer { onLabel(it.toJs()) })
        return { disposable.dispose() }
    }
}

private fun Reward.toJs() = RewardJs(
    id = id,
    furnitureId = furnitureId,
    furnitureLink = furnitureLink,
    todoDoneId = todoDoneId,
)

private fun RewardStore.State.toJs() = RewardStateJs(
    loading = loading,
    rewards = rewards.map { it.toJs() }.toTypedArray(),
    error = error,
)

private fun RewardStore.Label.toJs() = when (this) {
    is RewardStore.Label.ShowError -> RewardLabelJs("ERROR", message)
    RewardStore.Label.RefreshTodoDoneNotClaimed ->
        RewardLabelJs("REFRESH_TODO_DONE_NOT_CLAIMED", null)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun createRewardStore(): Promise<RewardStoreJs> {
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
        val apiService = ApiService(httpClient, networkMonitor)
        RewardStoreFactory(
            repository = RewardRepository(apiService),
            storage = storages.rewardStorage,
        ).create().also { it.init() }.let(::RewardStoreJs)
    }
}
