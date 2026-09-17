package bloomy.cozyspace.cache

import bloomy.cozyspace.data.dto.TodoRequestDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PendingAction {
    val id: String
    val createdAt: Long

    @Serializable
    @SerialName("create_todo")
    data class CreateTodo(
        override val id: String,
        override val createdAt: Long,
        val todo: TodoRequestDto,
    ) : PendingAction

    @Serializable
    @SerialName("complete_todo")
    data class CompleteTodo(
        override val id: String,
        override val createdAt: Long,
        val todoId: String,
    ) : PendingAction

    @Serializable
    @SerialName("modify_todo")
    data class ModifyTodo(
        override val id: String,
        override val createdAt: Long,
        val todoId: String,
        val todo: TodoRequestDto,
    ) : PendingAction

    @Serializable
    @SerialName("delete_todo")
    data class DeleteTodo(
        override val id: String,
        override val createdAt: Long,
        val todoId: String,
    ) : PendingAction
}
