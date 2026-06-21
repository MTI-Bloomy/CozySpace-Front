package bloomy.cozyspace.store

import bloomy.cozyspace.data.AuthentificationRepository
import bloomy.cozyspace.data.LoginDto
import bloomy.cozyspace.data.RegisterDto
import bloomy.cozyspace.domain.Token
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch
import kotlin.String

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
                        when (val result = repository.register(intent.request)) {
                            is ApiResult.Success -> {
                                dispatch(
                                    Msg.Register(
                                        result.data
                                    )
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(
                                    Msg.Error(result.message)
                                )
                            }

                            ApiResult.Empty -> {
                                dispatch(
                                    Msg.Error("Réponse vide du serveur")
                                )
                            }
                        }
                    }
                }

                is UserStore.Intent.Login -> {

                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.login(intent.request)) {
                            is ApiResult.Success -> {
                                dispatch(
                                    Msg.Login(
                                        result.data
                                    )
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(
                                    Msg.Error(result.message)
                                )
                            }

                            ApiResult.Empty -> {
                                dispatch(
                                    Msg.Error("Réponse vide du serveur")
                                )
                            }
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
