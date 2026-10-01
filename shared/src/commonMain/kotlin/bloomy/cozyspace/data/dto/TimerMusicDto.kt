package bloomy.cozyspace.data.dto

import bloomy.cozyspace.domain.TimerMusic
import kotlinx.serialization.Serializable

@Serializable
data class TimerMusicDto(
    val id: String,
    val link: String,
)

fun TimerMusicDto.toDomain(): TimerMusic = TimerMusic(
    id = id,
    link = link
)
