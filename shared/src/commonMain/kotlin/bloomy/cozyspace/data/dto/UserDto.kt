package bloomy.cozyspace.data.dto

import bloomy.cozyspace.domain.AiOptions
import bloomy.cozyspace.domain.User
import kotlinx.serialization.SerialName
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
    val displayName: String?,
    val email: String,
    val emailVerified: Boolean,
    val phoneNumber: String?,
    val photoUrl: String?,
    val providerData: List<ProviderDataDto>,
    val tokenValidAfterTimestamp: Long? = null,
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

@Serializable
data class RefreshDto(
    @SerialName("id_token") val idToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null
)

@Serializable
data class RefreshRequestDto(
    @SerialName("grant_type") val grantType: String = "refresh_token",
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
data class ParsedAiOptions(
    @SerialName("ai_cheer") val aiCheer: Boolean,
    @SerialName("ai_journal_prompts") val aiJournalPrompts: Boolean,
    @SerialName("ai_todo") val aiTodo: Boolean,
)

@Serializable
data class UserDto(
    val charaId: String?,
    val curHouseId: String?,
    val email: String,
    val houseSaveDelay: Int?,
    val id: String,
    val parsedAiOptions: ParsedAiOptions,
    val pseudo: String?,
    val retroDelay: Int?,
)

fun UserDto.toDomain(): User = User(
    id = id,
    email = email,
    pseudo = pseudo,
    charaId = charaId,
    curHouseId = curHouseId,
    houseSaveDelay = houseSaveDelay,
    retroDelay = retroDelay,
    aiOptions = AiOptions(
        aiCheer = parsedAiOptions.aiCheer,
        aiJournalPrompts = parsedAiOptions.aiJournalPrompts,
        aiTodo = parsedAiOptions.aiTodo,
    )
)
