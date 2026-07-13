package bloomy.cozyspace.store

data class Stores (
    val user : UserStore,
    val reward: RewardStore,
    val room: RoomStore,
    val house: HouseStore,
)
