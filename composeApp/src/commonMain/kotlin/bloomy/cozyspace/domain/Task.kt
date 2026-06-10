package bloomy.cozyspace.domain

import bloomy.cozyspace.utils.CategoryName
import kotlinx.serialization.Serializable

@Serializable
data class Task (
    val id: String,
    val name: String,
    val frequency: Int,
    val type: CategoryName, // Saved as String in db
    val startDate: String
)
