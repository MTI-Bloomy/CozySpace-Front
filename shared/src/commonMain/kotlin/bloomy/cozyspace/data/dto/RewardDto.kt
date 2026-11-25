package bloomy.cozyspace.data.dto

import bloomy.cozyspace.domain.Reward
import bloomy.cozyspace.utils.parseRoomType
import kotlinx.serialization.Serializable

@Serializable
data class RewardDto(
    val furnitureId: String,
    val furnitureLink: String,
    val id: String,
    val layer: Int,
    val placedInARoom: Boolean,
    val todoDoneId: String,
    val type: String,
)

fun RewardDto.toDomain(): Reward = Reward(
    id = id,
    furnitureId = furnitureId,
    furnitureLink = furnitureLink,
    todoDoneId = todoDoneId,
    layer = layer,
    placedInARoom = placedInARoom,
    roomType = parseRoomType(type),
)

@Serializable
data class ChooseRewardRequestDto(
    val todoDoneId: String,
    val placement: String,
)
