package bloomy.cozyspace.cache

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PendingAction {
    val id: String
    val createdAt: Long

    @Serializable
    @SerialName("save_house")
    data class SaveHouse(
        override val id: String,
        override val createdAt: Long,
        val houseId: String,
    ) : PendingAction

    @Serializable
    @SerialName("choose_reward")
    data class ChooseReward(
        override val id: String,
        override val createdAt: Long,
        val rewardId: String,
    ) : PendingAction
}
