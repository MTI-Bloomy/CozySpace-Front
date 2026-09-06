package bloomy.cozyspace.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import bloomy.cozyspace.AppEnvironment
import bloomy.cozyspace.data.*
import bloomy.cozyspace.network.ApiService
import bloomy.cozyspace.network.clearBearerCache
import bloomy.cozyspace.store.*

@Composable
fun rememberUserStore(env: AppEnvironment) =
    produceState<UserStore?>(initialValue = null, env.httpClient) {
        value = UserStoreFactory(
            repository = AuthentificationRepository(ApiService(env.httpClient)),
            storages = env.storages,
            onAuthStateChanged = { env.httpClient.clearBearerCache() }
        ).create().also { it.init() }
    }

@Composable
fun rememberHouseStore(env: AppEnvironment) =
    produceState<HouseStore?>(initialValue = null, env.httpClient) {
        value = HouseStoreFactory(
            repository = HouseRepository(ApiService(env.httpClient)),
            storage = env.storages.houseStorage,
        ).create().also { it.init() }
    }

@Composable
fun rememberRoomStore(env: AppEnvironment) =
    produceState<RoomStore?>(initialValue = null, env.httpClient) {
        value = RoomStoreFactory(
            repository = RoomRepository(ApiService(env.httpClient)),
            storage = env.storages.roomStorage,
        ).create().also { it.init() }
    }

@Composable
fun rememberRewardStore(env: AppEnvironment) =
    produceState<RewardStore?>(initialValue = null, env.httpClient) {
        value = RewardStoreFactory(
            repository = RewardRepository(ApiService(env.httpClient)),
            storage = env.storages.rewardStorage,
            assetRepository = AssetRepository(env.httpClient, env.storages.assetStorage),
        ).create().also { it.init() }
    }
