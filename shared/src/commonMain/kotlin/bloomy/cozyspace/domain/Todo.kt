package bloomy.cozyspace.domain

import bloomy.cozyspace.utils.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Todo(
    val id: String,
    val name: String,
    val type: RoomType,
    @Serializable(with = InstantSerializer::class)
    val date: Instant,
    val frequency: Int?,
    val rewardId: String? = null,
)
