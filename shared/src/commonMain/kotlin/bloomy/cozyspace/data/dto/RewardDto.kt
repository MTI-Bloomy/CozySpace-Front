package bloomy.cozyspace.data.dto

import bloomy.cozyspace.domain.Reward
import kotlinx.serialization.Serializable

@Serializable
data class RewardDto(
    val furnitureId: String,
    val furnitureLink: String,
    val id: String,
    val todoDoneId: String,
    val type: String,
)

fun RewardDto.toDomain(): Reward = Reward(
    id = id,
    furnitureId = furnitureId,
    furnitureLink = furnitureLink,
    todoDoneId = todoDoneId,
)

@Serializable
data class ChooseRewardRequestDto(
    val todoDoneId: String,
    val houseId: String,
    val roomId: String,
    val placement: String,
)
