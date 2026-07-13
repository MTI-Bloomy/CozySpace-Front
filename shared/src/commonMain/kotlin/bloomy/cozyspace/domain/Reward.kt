package bloomy.cozyspace.domain

import kotlinx.serialization.Serializable

@Serializable
data class Reward(
    val id: String,
    val furnitureId: String,
    val furnitureLink: String,
    val todoDoneId: String,
)
