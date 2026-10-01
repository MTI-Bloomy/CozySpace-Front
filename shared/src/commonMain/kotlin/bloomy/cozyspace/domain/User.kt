package bloomy.cozyspace.domain

import kotlinx.serialization.Serializable

@Serializable
data class AiOptions(
    val aiCheer: Boolean,
    val aiJournalPrompts: Boolean,
    val aiTodo: Boolean,
)

@Serializable
data class User(
    val id: String,
    val email: String,
    val pseudo: String?,
    val charaId: String?,
    val curHouseId: String?,
    val houseSaveDelay: Int?,
    val retroDelay: Int?,
    val aiOptions: AiOptions,
)
