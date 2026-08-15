package bloomy.cozyspace.cache

class UserStorage(store: KeyValueStore) {
    private val delegate = JsonCacheStore(store, "user_cache", UserCache.serializer())
    suspend fun save(cache: UserCache) = delegate.save(cache)
    suspend fun get(): UserCache? = delegate.get()
    suspend fun clear() = delegate.clear()
}

class HouseStorage(store: KeyValueStore) {
    private val delegate = JsonCacheStore(store, "house_cache", HouseCache.serializer())
    suspend fun save(cache: HouseCache) = delegate.save(cache)
    suspend fun get(): HouseCache? = delegate.get()
    suspend fun clear() = delegate.clear()
}

class RoomStorage(store: KeyValueStore) {
    private val delegate = JsonCacheStore(store, "room_cache", RoomCache.serializer())
    suspend fun save(cache: RoomCache) = delegate.save(cache)
    suspend fun get(): RoomCache? = delegate.get()
    suspend fun clear() = delegate.clear()
}

class RewardStorage(store: KeyValueStore) {
    private val delegate = JsonCacheStore(store, "reward_cache", RewardCache.serializer())
    suspend fun save(cache: RewardCache) = delegate.save(cache)
    suspend fun get(): RewardCache? = delegate.get()
    suspend fun clear() = delegate.clear()
}
