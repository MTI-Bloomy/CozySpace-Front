package bloomy.cozyspace.interfaces

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(
        val code: Int? = null,
        val message: String
    ) : ApiResult<Nothing>
    data object Empty : ApiResult<Nothing>
}
