package bloomy.cozyspace.cache

interface UserStorage {
    suspend fun save(cache: UserCache)
    suspend fun get(): UserCache?
    suspend fun clear()
}

expect fun createUserStorage(): UserStorage
