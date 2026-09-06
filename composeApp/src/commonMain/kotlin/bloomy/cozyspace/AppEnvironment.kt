package bloomy.cozyspace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import bloomy.cozyspace.cache.HouseStorage
import bloomy.cozyspace.cache.RewardStorage
import bloomy.cozyspace.cache.RoomStorage
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.cache.UserCache
import bloomy.cozyspace.cache.UserStorage
import bloomy.cozyspace.cache.createAssetStorage
import bloomy.cozyspace.cache.createKeyValueStore
import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.domain.User
import bloomy.cozyspace.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.MutableSharedFlow

class AppEnvironment(
    val storages: Storages,
    val httpClient: HttpClient,
    val forcedLogout: MutableSharedFlow<Unit>,
)

@Composable
fun rememberAppEnvironment(): AppEnvironment {
    val keyValueStore = remember { createKeyValueStore() }
    val assetStorage = remember { createAssetStorage() }
    val userStorage = remember { UserStorage(keyValueStore) }
    val houseStorage = remember { HouseStorage(keyValueStore) }
    val roomStorage = remember { RoomStorage(keyValueStore) }
    val rewardStorage = remember { RewardStorage(keyValueStore) }
    val forcedLogout = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

    val httpClient = remember {
        createHttpClient(
            baseUrl = Environment.API_URL,
            loadTokens = {
                userStorage.get()?.token
                    ?.takeIf { it.idToken.isNotBlank() }
                    ?.let { BearerTokens(it.idToken, it.refreshToken) }
            },
            onTokensRefreshed = { newToken ->
                val cache = userStorage.get()
                userStorage.save(UserCache(token = newToken, user = cache?.user ?: User("", "", "", null)))
            },
            onRefreshFailed = {
                userStorage.clear()
                forcedLogout.tryEmit(Unit)
            }
        )
    }

    return remember {
        AppEnvironment(
            storages = Storages(assetStorage, keyValueStore, userStorage, houseStorage, roomStorage, rewardStorage),
            httpClient = httpClient,
            forcedLogout = forcedLogout,
        )
    }
}
