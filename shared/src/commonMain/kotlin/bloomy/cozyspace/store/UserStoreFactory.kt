package bloomy.cozyspace.store

import bloomy.cozyspace.cache.UserCache
import bloomy.cozyspace.cache.UserStorage
import bloomy.cozyspace.data.AuthentificationRepository
import bloomy.cozyspace.data.dto.LoginDto
import bloomy.cozyspace.data.dto.LoginRequestDto
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
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
    private val onAuthStateChanged: suspend () -> Unit = {},
) {
    suspend fun create(): UserStore {
        val cache = storage.get()

        val initialState = UserStore.State(
            token = cache?.token ?: Token("", ""),
            user = cache?.user ?: User("", "", "", null),
        )

        return object : UserStore,
            Store<UserStore.Intent, UserStore.State, UserStore.Label> by storeFactory.create(
                name = "UserStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data object Logout : Msg
        data object Register : Msg

        data class Login(val response: LoginDto) : Msg

        data class Error(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
        UserStore.Intent,
        Unit,
        UserStore.State,
        Msg,
        UserStore.Label,
        >() {

        override fun executeIntent(
            intent: UserStore.Intent,
        ) {
            when (intent) {
                is UserStore.Intent.Logout -> {
                    scope.launch {
                        storage.clear()
                        onAuthStateChanged()

                        dispatch(Msg.Logout)
                        publish(UserStore.Label.Logout)
                    }
                }

                is UserStore.Intent.Register -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.register(intent.request)) {
                            is ApiResult.Success -> {
                                dispatch(Msg.Register)

                                login(
                                    LoginRequestDto(
                                        email = intent.request.email,
                                        password = intent.request.password,
                                    ),
                                )
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(UserStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(UserStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is UserStore.Intent.Login -> {
                    dispatch(Msg.Loading)
                    scope.launch { login(intent.request) }
                }
            }
        }

        private suspend fun login(request: LoginRequestDto) {
            when (val result = repository.login(request)) {
                is ApiResult.Success -> {
                    dispatch(Msg.Login(result.data))

                    storage.save(
                        UserCache(
                            token = Token(
                                result.data.idToken,
                                result.data.refreshToken.orEmpty(),
                            ),
                            user = state().user,
                        ),
                    )

                    publish(UserStore.Label.LoginSuccess)
                }

                is ApiResult.Error -> {
                    dispatch(Msg.Error(result.message))
                    publish(UserStore.Label.ShowError(result.message))
                }

                ApiResult.Empty -> {
                    dispatch(Msg.Error("Empty response from server"))
                    publish(UserStore.Label.ShowError("Empty response from server"))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<UserStore.State, Msg> {
        override fun UserStore.State.reduce(msg: Msg): UserStore.State =
            when (msg) {
                Msg.Loading ->
                    copy(
                        loading = true,
                        error = null,
                    )

                Msg.Logout ->
                    UserStore.State()

                is Msg.Register ->
                    copy(
                        loading = false,
                        error = null,
                    )

                is Msg.Login ->
                    copy(
                        loading = false,
                        token = Token(
                            idToken = msg.response.idToken,
                            refreshToken = msg.response.refreshToken.orEmpty(),
                        ),
                        error = null,
                    )

                is Msg.Error ->
                    copy(
                        loading = false,
                        error = msg.message,
                    )
            }
    }
}
