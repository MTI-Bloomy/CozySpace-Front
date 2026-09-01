package bloomy.cozyspace.store

import bloomy.cozyspace.domain.Room
import com.arkivanov.mvikotlin.core.store.Store

interface RoomStore : Store<RoomStore.Intent, RoomStore.State, RoomStore.Label> {
    sealed interface Intent {
        data class GetRooms(
            val houseId: String,
            val saveMode: Boolean = false,
        ) : Intent
    }

    sealed interface Label {
        data class ShowError(val message: String) : Label
    }

    data class State(
        val loading: Boolean = false,
        val rooms: List<Room> = emptyList(),
        val error: String? = null,
    )
}
