package bloomy.cozyspace.data.dto

import bloomy.cozyspace.domain.House
import bloomy.cozyspace.domain.Reward
import bloomy.cozyspace.utils.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class HouseDto(
    val id: String,
    @Serializable(with = InstantSerializer::class)
    val saveDate: Instant?,
    val name: String,
    val rooms: List<String>,
)

fun HouseDto.toDomain(): House = House(
    id = id,
    name = name,
    rooms = rooms,
    saveDate = saveDate,
)
