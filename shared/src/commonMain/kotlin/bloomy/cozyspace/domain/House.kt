package bloomy.cozyspace.domain

import bloomy.cozyspace.utils.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class House(
    val id: String,
    val name: String,
    val rooms: List<String>,
    @Serializable(with = InstantSerializer::class)
    val saveDate: Instant?
)
