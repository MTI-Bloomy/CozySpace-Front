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
    val house: House? = null,
    val savedHouses: List<House> = emptyList()
)

@Serializable
data class RoomCache(
    val rooms: List<Room> = emptyList()
)

@Serializable
data class RewardCache(
    val rewards: List<Reward> = emptyList()
)

@Serializable
data class TodoListCache(
    val todoList: List<Todo> = emptyList()
)

@Serializable
data class TodoDoneCache(
    val todoDone: List<Todo> = emptyList(),
    val todoDoneNotClaimed: List<Todo> = emptyList()
)

@Serializable
data class TimerMusicCache(
    val timersMusics: List<TimerMusic> = emptyList()
)
