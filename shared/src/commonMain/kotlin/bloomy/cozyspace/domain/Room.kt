package bloomy.cozyspace.domain

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: String,
    val maxCapacity: Int,
    val type: RoomType,
    val furniture: List<String>,
)
