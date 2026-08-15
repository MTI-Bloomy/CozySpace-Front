package bloomy.cozyspace.cache

import bloomy.cozyspace.domain.House
import bloomy.cozyspace.domain.Reward
import bloomy.cozyspace.domain.Room
import bloomy.cozyspace.domain.Token
import bloomy.cozyspace.domain.User
import kotlinx.serialization.Serializable

@Serializable
data class UserCache(
    val token: Token,
    val user: User
)

@Serializable
data class HouseCache(
    val house: House?,
    val savedHouses: List<House>
)

@Serializable
data class RoomCache(
    val rooms: List<Room>
)

@Serializable
data class RewardCache(
    val rewards: List<Reward>
)
