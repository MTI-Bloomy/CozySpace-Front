package bloomy.cozyspace.store

import bloomy.cozyspace.data.AuthentificationRepository
import bloomy.cozyspace.data.LoginDto
import bloomy.cozyspace.data.LoginRequestDto
import bloomy.cozyspace.data.RegisterDto
import bloomy.cozyspace.data.RegisterRequestDto
import bloomy.cozyspace.domain.User
import bloomy.cozyspace.domain.Token
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class UserStoreFactory(
    private val repository: AuthentificationRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) {

    fun create(): UserStore =
        object : UserStore,
            com.arkivanov.mvikotlin.core.store.Store<
                UserStore.Intent,
                UserStore.State,
                Nothing
                > by storeFactory.create(
                name = "UserStore",
                initialState = UserStore.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data object Loading : Msg

        data class Register(
            val response: RegisterDto
        ) : Msg

        data class Login(
            val response: LoginDto
        ) : Msg

        data class Error(
            val message: String
        ) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
            UserStore.Intent,
            Unit,
            UserStore.State,
            Msg,
            Nothing>() {

        override fun executeIntent(
            intent: UserStore.Intent
        ) {

            when (intent) {

                is UserStore.Intent.Register -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        try {

                            val response = repository.register(intent.request)

                            dispatch(
                                Msg.Register(
                                    response
                                )
                            )

                        } catch (exception: Exception) {

                            dispatch(
                                Msg.Error(
                                    exception.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }

                is UserStore.Intent.Login -> {

                    dispatch(Msg.Loading)

                    scope.launch {

                        try {

                            val response = repository.login(intent.request)

                            dispatch(
                                Msg.Login(
                                    response
                                )
                            )

                        } catch (exception: Exception) {

                            dispatch(
                                Msg.Error(
                                    exception.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl :
        Reducer<UserStore.State, Msg> {

        override fun UserStore.State.reduce(msg: Msg): UserStore.State =

            when (msg) {

                Msg.Loading ->
                    copy(
                        loading = true,
                        error = null
                    )

                is Msg.Register ->
                    copy(
                        loading = false,
                        /*
                        user = User(
                            uid = msg.response.uid,
                            email = msg.response.email,
                            displayName = msg.response.displayName,
                            photoUrl = msg.response.photoUrl
                        ),
                        */
                        error = null
                    )

                is Msg.Login ->
                    copy(
                        loading = false,
                        token = Token(
                            idToken = msg.response.idToken,
                            refreshToken = msg.response.refreshToken.orEmpty()
                        ),
                        error = null
                    )

                is Msg.Error ->
                    copy(
                        loading = false,
                        error = msg.message
                    )
            }
    }
}
