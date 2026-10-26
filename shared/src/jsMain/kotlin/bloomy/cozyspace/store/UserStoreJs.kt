package bloomy.cozyspace.store

import bloomy.cozyspace.cache.HouseStorage
import bloomy.cozyspace.cache.RewardStorage
import bloomy.cozyspace.cache.RoomStorage
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.cache.SyncQueueStorage
import bloomy.cozyspace.cache.TodoDoneStorage
import bloomy.cozyspace.cache.TodoListStorage
import bloomy.cozyspace.cache.UserStorage
import bloomy.cozyspace.cache.createAssetStorage
import bloomy.cozyspace.cache.createKeyValueStore
import bloomy.cozyspace.data.AuthentificationRepository
import bloomy.cozyspace.data.dto.LoginRequestDto
import bloomy.cozyspace.data.dto.RegisterRequestDto
import bloomy.cozyspace.network.ApiService
import bloomy.cozyspace.network.NetworkMonitor
import bloomy.cozyspace.network.clearBearerCache
import bloomy.cozyspace.network.createHttpClient
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.store.UserStoreFactory
import com.arkivanov.mvikotlin.core.rx.observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.Promise

@OptIn(ExperimentalJsExport::class)
@JsExport
class UserStateJs(
    val loading: Boolean,
    val isLoggedIn: Boolean,
    val error: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class UserLabelJs(
    val type: String,
    val message: String?,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
class UserStoreJs internal constructor(
    private val store: UserStore,
) {
    fun login(email: String, password: String) {
        store.accept(UserStore.Intent.Login(LoginRequestDto(email, password)))
    }

    fun register(email: String, password: String) {
        store.accept(UserStore.Intent.Register(RegisterRequestDto(email, password)))
    }

    fun logout() {
        store.accept(UserStore.Intent.Logout)
    }

    fun subscribeState(onState: (UserStateJs) -> Unit): () -> Unit {
        val disposable = store.states(observer { onState(it.toJs()) })
        return { disposable.dispose() }
    }

    fun subscribeLabels(onLabel: (UserLabelJs) -> Unit): () -> Unit {
        val disposable = store.labels(observer { onLabel(it.toJs()) })
        return { disposable.dispose() }
    }
}

private fun UserStore.State.toJs() = UserStateJs(
    loading = loading,
    isLoggedIn = token.idToken.isNotBlank(),
    error = error,
)

private fun UserStore.Label.toJs() = when (this) {
    is UserStore.Label.ShowError -> UserLabelJs("ERROR", message)
    UserStore.Label.LoginSuccess -> UserLabelJs("LOGIN_SUCCESS", null)
    UserStore.Label.Logout -> UserLabelJs("LOGOUT", null)
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun createUserStore(): Promise<UserStoreJs> {
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
            syncQueueStorage = SyncQueueStorage(keyValueStore),
        )
        val httpClient = createHttpClient(
            baseUrl = bloomy.cozyspace.config.Environment.API_URL,
            loadTokens = {
                storages.userStorage.get()?.token
                    ?.takeIf { it.idToken.isNotBlank() }
                    ?.let { io.ktor.client.plugins.auth.providers.BearerTokens(it.idToken, it.refreshToken) }
            },
            onTokensRefreshed = { token ->
                val cache = storages.userStorage.get()
                storages.userStorage.save(
                    bloomy.cozyspace.cache.UserCache(
                        token = token,
                        user = cache?.user ?: bloomy.cozyspace.domain.User("", "", "", null),
                    ),
                )
            },
            onRefreshFailed = {
                storages.userStorage.clear()
            },
        )
        val networkMonitor = NetworkMonitor(httpClient, scope)
        UserStoreFactory(
            repository = AuthentificationRepository(ApiService(httpClient, networkMonitor)),
            storages = storages,
            onAuthStateChanged = { httpClient.clearBearerCache() },
        ).create().also { it.init() }.let(::UserStoreJs)
    }
}
