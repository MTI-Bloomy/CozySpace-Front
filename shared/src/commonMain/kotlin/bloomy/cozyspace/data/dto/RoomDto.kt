package bloomy.cozyspace.data.dto

import bloomy.cozyspace.domain.Reward
import bloomy.cozyspace.domain.Room
import bloomy.cozyspace.domain.RoomType
import kotlinx.serialization.Serializable

@Serializable
data class RoomDto(
    val id: String,
    val maxCapacity: Int,
    val rewards: List<String>,
    val type: String,
)

fun RoomDto.toDomain(): Room = Room(
    id = id,
    maxCapacity = maxCapacity,
    type = when (type) {
        "Garden" -> RoomType.GARDEN
        "Work" -> RoomType.WORK
        "Bedroom" -> RoomType.BEDROOM
        "Bathroom" -> RoomType.BATHROOM
        "Kitchen" -> RoomType.KITCHEN
        else -> throw IllegalArgumentException("Unknown room type: $type")
    },
    furniture = rewards,
)
