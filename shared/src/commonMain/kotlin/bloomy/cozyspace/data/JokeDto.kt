package bloomy.cozyspace.data

import kotlinx.serialization.Serializable

@Serializable
data class JokeDto(
    val id: Int,
    val type: String,
    val joke: String,
    val answer: String
)
