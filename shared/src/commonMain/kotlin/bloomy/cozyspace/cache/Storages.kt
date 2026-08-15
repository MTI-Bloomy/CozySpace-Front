package bloomy.cozyspace.cache

data class Storages(
    val assetStorage: AssetStorage,
    val keyValueStore: KeyValueStore,

    val userStorage: UserStorage,
    val houseStorage: HouseStorage,
    val roomStorage: RoomStorage,
    val rewardStorage: RewardStorage
)
