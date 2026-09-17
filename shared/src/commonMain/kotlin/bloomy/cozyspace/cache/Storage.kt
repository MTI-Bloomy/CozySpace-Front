package bloomy.cozyspace.cache

import kotlinx.serialization.builtins.ListSerializer

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

class TodoListStorage(store: KeyValueStore) {
    private val delegate = JsonCacheStore(store, "todo_list_cache", TodoListCache.serializer())
    suspend fun save(cache: TodoListCache) = delegate.save(cache)
    suspend fun get(): TodoListCache? = delegate.get()
    suspend fun clear() = delegate.clear()
}

class TodoDoneStorage(store: KeyValueStore) {
    private val delegate = JsonCacheStore(store, "todo_done_cache", TodoDoneCache.serializer())
    suspend fun save(cache: TodoDoneCache) = delegate.save(cache)
    suspend fun get(): TodoDoneCache? = delegate.get()
    suspend fun clear() = delegate.clear()
}

class SyncQueueStorage(store: KeyValueStore) {
    private val delegate = JsonCacheStore(
        store = store,
        key = "sync_queue_cache",
        serializer = ListSerializer(PendingAction.serializer()),
    )

    suspend fun save(actions: List<PendingAction>) = delegate.save(actions)
    suspend fun get(): List<PendingAction> = delegate.get() ?: emptyList()
    suspend fun clear() = delegate.clear()
}
