package bloomy.cozyspace.cache

import bloomy.cozyspace.domain.*
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

@Serializable
data class TodoListCache(
    val todoList: List<Todo>
)

@Serializable
data class TodoDoneCache(
    val todoDone: List<Todo>
)

@Serializable
data class TimerMusicCache(
    val timersMusics: List<TimerMusic> = emptyList()
)
