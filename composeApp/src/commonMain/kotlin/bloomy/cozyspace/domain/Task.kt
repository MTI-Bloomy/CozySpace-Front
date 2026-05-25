package bloomy.cozyspace.domain

import kotlinx.serialization.Serializable

@Serializable
data class Task (
    val id: String,
    val name: String,
    val frequency: Int,
    val type: String,
    val startDate: String
)
