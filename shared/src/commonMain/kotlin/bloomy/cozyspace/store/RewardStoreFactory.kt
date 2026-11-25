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
    private val assetRepository: AssetRepository? = null,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): RewardStore {
        val cache = storage.get()

        val initialState = RewardStore.State(
            rewards = cache?.rewards ?: emptyList(),
            inventory = cache?.inventory ?: emptyList(),
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
        data object Offline : Msg
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
                                            assetRepository?.getAsset(it.furnitureId, it.furnitureLink)
                                        } catch (e: Exception) {
                                            // TODO: To handle, but not necessary for now
                                        }
                                    }

                                    it.toDomain()
                                }

                                storage.save(
                                    RewardCache(
                                        rewards.filter { it.placedInARoom }.sortedBy { it.layer },
                                        rewards.filterNot { it.placedInARoom }
                                    )
                                )

                                dispatch(Msg.GetRewardsSuccess(rewards))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(RewardStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                is RewardStore.Intent.GetReward -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getReward(intent.id)) {
                            is ApiResult.Success -> {
                                val reward = result.data.toDomain()

                                updateReward(reward)

                                dispatch(Msg.GetRewardSuccess(reward))

                                launch {
                                    try {
                                        assetRepository?.getAsset(reward.furnitureId, reward.furnitureLink)
                                    } catch (e: Exception) {
                                        // TODO: To handle, but not necessary for now
                                    }
                                }
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(RewardStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                is RewardStore.Intent.ChooseReward -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.chooseReward(intent.request)) {
                            is ApiResult.Success -> {
                                val reward = result.data.toDomain()

                                updateReward(reward)

                                publish(RewardStore.Label.RefreshTodoDoneNotClaimed)
                                dispatch(Msg.ChooseRewardSuccess(reward))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(RewardStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }

                RewardStore.Intent.Clear -> dispatch(Msg.Clear)
            }
        }

        private suspend fun updateReward(reward: Reward) {
            val cache = storage.get()

            storage.save(
                RewardCache(
                    rewards = if (reward.placedInARoom) {
                        (cache?.rewards.orEmpty().filterNot { it.id == reward.id } + reward).sortedBy { it.layer }
                    } else {
                        cache?.rewards.orEmpty()
                    },
                    inventory = if (!reward.placedInARoom) {
                        cache?.inventory.orEmpty().filterNot { it.id == reward.id } + reward
                    } else {
                        cache?.inventory.orEmpty()
                    }
                )
            )
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

                Msg.Offline -> copy(
                    loading = false,
                    error = null,
                )

                is Msg.GetRewardsSuccess -> copy(
                    loading = false,
                    rewards = msg.rewards.filter { it.placedInARoom }.sortedBy { it.layer },
                    inventory = msg.rewards.filterNot { it.placedInARoom },
                    error = null,
                )

                is Msg.GetRewardSuccess, is Msg.ChooseRewardSuccess -> {
                    val reward = when (msg) {
                        is Msg.GetRewardSuccess -> msg.reward
                        is Msg.ChooseRewardSuccess -> msg.reward
                    }

                    copy(
                        loading = false,
                        rewards = if (reward.placedInARoom) { (rewards.filterNot { it.id == reward.id } + reward).sortedBy { it.layer } } else { rewards },
                        inventory = if (!reward.placedInARoom) { inventory.filterNot { it.id == reward.id } + reward } else { inventory },
                        error = null,
                    )
                }

                is Msg.Error ->
                    copy(
                        loading = false,
                        error = msg.message,
                    )

                Msg.Clear -> RewardStore.State()
            }
    }
}
