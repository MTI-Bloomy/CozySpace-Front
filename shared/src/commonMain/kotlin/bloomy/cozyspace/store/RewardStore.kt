package bloomy.cozyspace.store

import bloomy.cozyspace.data.dto.ChooseRewardRequestDto
import bloomy.cozyspace.domain.Reward
import com.arkivanov.mvikotlin.core.store.Store

interface RewardStore : Store<RewardStore.Intent, RewardStore.State, RewardStore.Label> {
    sealed interface Intent {
        data object GetRewards : Intent
        data class GetReward(val id: String) : Intent
        data class ChooseReward(
            val request: ChooseRewardRequestDto,
        ) : Intent
    }

    sealed interface Label {
        data class ShowError(val message: String) : Label
    }

    data class State(
        val loading: Boolean = false,
        val rewards: List<Reward> = emptyList(),
        val images: Map<String, ByteArray> = emptyMap(),
        val error: String? = null,
    )
}

