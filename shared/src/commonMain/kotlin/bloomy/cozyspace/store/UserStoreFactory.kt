package bloomy.cozyspace.store

import bloomy.cozyspace.cache.UserCache
import bloomy.cozyspace.cache.UserStorage
import bloomy.cozyspace.data.AuthentificationRepository
import bloomy.cozyspace.data.LoginDto
import bloomy.cozyspace.data.RegisterDto
import bloomy.cozyspace.domain.Token
import bloomy.cozyspace.domain.User
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch
import kotlin.String

class UserStoreFactory(
    private val repository: AuthentificationRepository,
    private val storage: UserStorage,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) {

    suspend fun create(): UserStore {
        val cache = storage.get()

        val initialState = UserStore.State(
            token = cache?.token ?: Token("", ""),
            user = cache?.user ?: User("", "", "", null)
        )

        return object : UserStore,
            Store<UserStore.Intent, UserStore.State, UserStore.Label> by storeFactory.create(
                name = "UserStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}
    }

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
            UserStore.Label>() {

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

                                storage.save(
                                    UserCache(
                                        token = state().token,
                                        user = state().user
                                    )
                                )

                                publish(
                                    UserStore.Label.RegisterSuccess
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(
                                    Msg.Error(result.message)
                                )

                                publish(
                                    UserStore.Label.ShowError(
                                        result.message
                                    )
                                )
                            }

                            ApiResult.Empty -> {
                                dispatch(
                                    Msg.Error("Réponse vide du serveur")
                                )

                                publish(UserStore.Label.ShowError("Réponse vide du serveur"))
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

                                storage.save(
                                    UserCache(
                                        token = Token(
                                            result.data.idToken,
                                            result.data.refreshToken.orEmpty()
                                        ),
                                        user = state().user
                                    )
                                )

                                publish(
                                    UserStore.Label.LoginSuccess
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(
                                    Msg.Error(result.message)
                                )

                                publish(
                                    UserStore.Label.ShowError(
                                        result.message
                                    )
                                )
                            }

                            ApiResult.Empty -> {
                                dispatch(
                                    Msg.Error("Réponse vide du serveur")
                                )

                                publish(UserStore.Label.ShowError("Réponse vide du serveur"))
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
