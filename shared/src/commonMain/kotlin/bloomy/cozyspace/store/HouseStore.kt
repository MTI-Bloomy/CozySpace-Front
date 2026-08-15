package bloomy.cozyspace.store

import bloomy.cozyspace.domain.House
import com.arkivanov.mvikotlin.core.store.Store

interface HouseStore : Store<HouseStore.Intent, HouseStore.State, HouseStore.Label> {
    sealed interface Intent {
        data object GetHouse : Intent
        data class CreateHouse(val name: String) : Intent
    }

    sealed interface Label {
        data class ShowError(val message: String): Label
    }

    data class State(
        val loading: Boolean = false,
        val house: House? = null,
        val savedHouses: List<House> = emptyList(),
        val error: String? = null
    )
}
