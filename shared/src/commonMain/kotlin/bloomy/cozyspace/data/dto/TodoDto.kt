package bloomy.cozyspace.data.dto

import bloomy.cozyspace.domain.Reward
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.utils.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class TodoDto (
    @Serializable(with = InstantSerializer::class)
    val date: Instant,
    val frequency: Int?,
    val id: String,
    val name: String,
    val rewardId: String?,
    val type: String,
)

fun TodoDto.toDomain(): Todo = Todo(
    id = id,
    name = name,
    type = when (type) {
        "Garden" -> RoomType.GARDEN
        "Work" -> RoomType.WORK
        "Bedroom" -> RoomType.BEDROOM
        "Bathroom" -> RoomType.BATHROOM
        "Kitchen" -> RoomType.KITCHEN
        else -> throw IllegalArgumentException("Unknown room type: $type")
    },
    date = date,
    frequency = frequency,
    rewardId = rewardId,
)

@Serializable
data class TodoRequestDto (
    val name: String,
    val frequency: Int,
    @Serializable(with = InstantSerializer::class)
    val nextDueDate: Instant,
    val type: String,
)
