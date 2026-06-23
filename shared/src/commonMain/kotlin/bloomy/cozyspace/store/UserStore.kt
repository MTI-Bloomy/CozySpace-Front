package bloomy.cozyspace.store

import bloomy.cozyspace.domain.User
import bloomy.cozyspace.domain.Token
import com.arkivanov.mvikotlin.core.store.Store

interface UserStore :
    Store<UserStore.Intent, UserStore.State, UserStore.Label> {

    sealed interface Intent {
        data class Register(
            val request: bloomy.cozyspace.data.RegisterRequestDto
        ) : Intent

        data class Login(
            val request: bloomy.cozyspace.data.LoginRequestDto
        ) : Intent
    }

    sealed interface Label {
        data class ShowError(val message: String) : Label
        data object LoginSuccess : Label
        data object RegisterSuccess : Label
    }

    data class State(
        val loading: Boolean = false,
        val token: Token = Token("", ""),
        val user: User = User("", "", "", null),
        val error: String? = null
    )
}
