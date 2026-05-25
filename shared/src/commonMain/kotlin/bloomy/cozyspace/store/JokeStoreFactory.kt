package bloomy.cozyspace.store

import bloomy.cozyspace.data.JokeRepository
import bloomy.cozyspace.domain.Joke
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class JokeStoreFactory(
    private val repository: JokeRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) {

    fun create(): JokeStore =
        object : JokeStore,
            com.arkivanov.mvikotlin.core.store.Store<
                JokeStore.Intent,
                JokeStore.State,
                Nothing
                > by storeFactory.create(
                name = "JokeStore",
                initialState = JokeStore.State(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data object LoadingDev : Msg

        data class Loaded(
            val joke: String,
            val answer: String
        ) : Msg

        data class Error(
            val message: String
        ) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
            JokeStore.Intent,
            Unit,
            JokeStore.State,
            Msg,
            Nothing>() {

        override fun executeIntent(
            intent: JokeStore.Intent
        ) {

            when (intent) {

                JokeStore.Intent.LoadJoke -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        try {

                            val joke = repository.getRandomJoke()

                            dispatch(
                                Msg.Loaded(
                                    joke.joke,
                                    joke.answer
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

                JokeStore.Intent.LoadDevJoke -> {

                    dispatch(Msg.LoadingDev)

                    scope.launch {

                        try {

                            val joke = repository.getRandomDevJoke()

                            dispatch(
                                Msg.Loaded(
                                    joke.joke,
                                    joke.answer
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
        Reducer<JokeStore.State, Msg> {

        override fun JokeStore.State.reduce(msg: Msg): JokeStore.State =

            when (msg) {

                Msg.Loading ->
                    copy(
                        loading = true,
                        error = null
                    )

                Msg.LoadingDev ->
                    copy(
                        loading = true,
                        error = null
                    )

                is Msg.Loaded ->
                    copy(
                        loading = false,
                        joke = Joke(msg.joke, msg.answer),
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
