package bloomy.cozyspace.store

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
    private val assetRepository: AssetRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): RewardStore {
        val initialState = RewardStore.State(
            rewards = emptyList(),
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
        data class ImageLoaded(val rewardId: String, val bytes: ByteArray) : Msg
        data class Error(val message: String) : Msg
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
                                dispatch(
                                    Msg.GetRewardsSuccess(
                                        result.data.map {
                                            launch {
                                                try {
                                                    val bytes =
                                                        assetRepository.getAsset(it.furnitureId, it.furnitureLink)
                                                    dispatch(Msg.ImageLoaded(it.id, bytes))
                                                } catch (e: Exception) {
                                                    // pas d'image = pas bloquant, le reward reste affiché (placeholder côté UI)
                                                }
                                            }

                                            it.toDomain()
                                        },
                                    ),
                                )
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
                                dispatch(Msg.GetRewardSuccess(reward))

                                launch {
                                    try {
                                        val bytes = assetRepository.getAsset(reward.furnitureId, reward.furnitureLink)
                                        dispatch(Msg.ImageLoaded(reward.id, bytes))
                                    } catch (e: Exception) {
                                        // pas d'image = pas bloquant, le reward reste affiché (placeholder côté UI)
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
                                dispatch(Msg.ChooseRewardSuccess(result.data.toDomain()))
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

                is Msg.ImageLoaded -> copy(images = images + (msg.rewardId to msg.bytes))

                is Msg.Error ->
                    copy(
                        loading = false,
                        error = msg.message,
                    )
            }
    }
}
