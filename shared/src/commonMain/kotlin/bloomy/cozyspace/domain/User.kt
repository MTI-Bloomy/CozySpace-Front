package bloomy.cozyspace.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String?,
)
