package bloomy.cozyspace.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProviderDataDto(
    val displayName: String?,
    val email: String,
    val phoneNumber: String?,
    val photoUrl: String?,
    val providerId: String,
    val uid: String
)

@Serializable
data class UserMetadataDto(
    val creationTimestamp: Long,
    val lastRefreshTimestamp: Long,
    val lastSignInTimestamp: Long
)

@Serializable
data class RegisterDto(
    val displayName: String,
    val email: String,
    val emailVerified: Boolean,
    val phoneNumber: String?,
    val photoUrl: String?,
    val providerData: List<ProviderDataDto>,
    val tokenValidAfterTimestamp: Long,
    val uid: String,
    val userMetadata: UserMetadataDto
)

@Serializable
data class LoginDto(
    val idToken: String,
    val refreshToken: String?,
)

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)
