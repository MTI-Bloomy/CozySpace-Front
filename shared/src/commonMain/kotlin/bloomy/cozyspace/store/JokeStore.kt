package bloomy.cozyspace.store

import bloomy.cozyspace.domain.Joke
import com.arkivanov.mvikotlin.core.store.Store

interface JokeStore :
    Store<JokeStore.Intent, JokeStore.State, Nothing> {

    sealed interface Intent {
        data object LoadJoke : Intent
        data object LoadDevJoke : Intent
    }

    data class State(
        val loading: Boolean = false,
        val joke: Joke = Joke("", ""),
        val error: String? = null
    )
}
