package bloomy.cozyspace.store

import bloomy.cozyspace.domain.TimerMusic
import com.arkivanov.mvikotlin.core.store.Store

interface TimerMusicStore : Store<TimerMusicStore.Intent, TimerMusicStore.State, TimerMusicStore.Label> {
    sealed interface Intent {
        data object GetTimersMusics : Intent
    }

    sealed interface Label {
        data class ShowError(val message: String) : Label
    }

    data class State(
        val loading: Boolean = false,
        val timersMusics: List<TimerMusic> = emptyList(),
        val error: String? = null,
    )
}
