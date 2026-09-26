package bloomy.cozyspace.domain

import kotlinx.serialization.Serializable

@Serializable
data class TimerMusic(
    val id: String,
    val link: String,
)
