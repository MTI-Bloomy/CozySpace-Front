package bloomy.cozyspace.store

import bloomy.cozyspace.cache.RewardCache
import bloomy.cozyspace.cache.RewardStorage
import bloomy.cozyspace.data.AssetRepository
import bloomy.cozyspace.data.RewardRepository
import bloomy.cozyspace.data.dto.toDomain
import bloomy.cozyspace.domain.Reward
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class RewardStoreFactory(
    private val repository: RewardRepository,
    private val storage: RewardStorage,
    private val assetRepository: AssetRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): RewardStore {
        val cache = storage.get()

        val initialState = RewardStore.State(
            rewards = cache?.rewards ?: emptyList(),
        )

        return object : RewardStore,
            Store<RewardStore.Intent, RewardStore.State, RewardStore.Label> by storeFactory.create(
                name = "RewardStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class GetRewardsSuccess(val rewards: List<Reward>) : Msg
        data class GetRewardSuccess(val reward: Reward) : Msg
        data class ChooseRewardSuccess(val reward: Reward) : Msg
        data class Error(val message: String) : Msg
        data object Clear : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
        RewardStore.Intent,
        Unit,
        RewardStore.State,
        Msg,
        RewardStore.Label,
        >() {

        override fun executeIntent(intent: RewardStore.Intent) {
            when (intent) {
                RewardStore.Intent.GetRewards -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getRewards()) {
                            is ApiResult.Success -> {
                                val rewards = result.data.map {
                                    launch {
                                        try {
                                            assetRepository.getAsset(it.furnitureId, it.furnitureLink)
                                        } catch (e: Exception) {
                                            // TODO: To handle, but not necessary for now
                                        }
                                    }

                                    it.toDomain()
                                }

                                storage.save(RewardCache(rewards))

                                dispatch(Msg.GetRewardsSuccess(rewards))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(RewardStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(RewardStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is RewardStore.Intent.GetReward -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getReward(intent.id)) {
                            is ApiResult.Success -> {
                                val reward = result.data.toDomain()

                                val cache = storage.get()

                                storage.save(
                                    if (cache?.rewards.isNullOrEmpty()) {
                                        RewardCache(listOf(reward))
                                    } else {
                                        RewardCache(
                                            if (cache.rewards.any { it.id == reward.id }) cache.rewards.map { if (it.id == reward.id) reward else it } else cache.rewards + reward
                                        )
                                    }
                                )

                                dispatch(Msg.GetRewardSuccess(reward))

                                launch {
                                    try {
                                        assetRepository.getAsset(reward.furnitureId, reward.furnitureLink)
                                    } catch (e: Exception) {
                                        // TODO: To handle, but not necessary for now
                                    }
                                }
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(RewardStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(RewardStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is RewardStore.Intent.ChooseReward -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.chooseReward(intent.request)) {
                            is ApiResult.Success -> {
                                val reward = result.data.toDomain()

                                val cache = storage.get()

                                storage.save(
                                    if (cache?.rewards.isNullOrEmpty()) {
                                        RewardCache(listOf(reward))
                                    } else {
                                        RewardCache(
                                            if (cache.rewards.any { it.id == reward.id }) cache.rewards.map { if (it.id == reward.id) reward else it } else cache.rewards + reward
                                        )
                                    }
                                )

                                dispatch(Msg.ChooseRewardSuccess(reward))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(RewardStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(RewardStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }
                RewardStore.Intent.Clear -> dispatch(Msg.Clear)
            }
        }
    }

    private object ReducerImpl : Reducer<RewardStore.State, Msg> {
        override fun RewardStore.State.reduce(msg: Msg): RewardStore.State =
            when (msg) {
                Msg.Loading ->
                    copy(
                        loading = true,
                        error = null,
                    )

                is Msg.GetRewardsSuccess -> copy(
                    loading = false,
                    rewards = msg.rewards,
                    error = null,
                )

                is Msg.GetRewardSuccess -> copy(
                    loading = false,
                    rewards = if (rewards.any { it.id == msg.reward.id }) rewards.map { if (it.id == msg.reward.id) msg.reward else it } else rewards + msg.reward,
                    error = null,
                )

                is Msg.ChooseRewardSuccess -> copy(
                    loading = false,
                    rewards = if (rewards.any { it.id == msg.reward.id }) rewards.map { if (it.id == msg.reward.id) msg.reward else it } else rewards + msg.reward,
                    error = null,
                )

                is Msg.Error ->
                    copy(
                        loading = false,
                        error = msg.message,
                    )

                is Msg.Clear -> RewardStore.State()
            }
    }
}
