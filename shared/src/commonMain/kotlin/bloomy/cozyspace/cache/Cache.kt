package bloomy.cozyspace.cache

import bloomy.cozyspace.domain.Token
import bloomy.cozyspace.domain.User
import kotlinx.serialization.Serializable

@Serializable
data class UserCache(
    val token: Token,
    val user: User
)
