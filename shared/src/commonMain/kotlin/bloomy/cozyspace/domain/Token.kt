package bloomy.cozyspace.domain

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val idToken: String,
    val refreshToken: String,
)
