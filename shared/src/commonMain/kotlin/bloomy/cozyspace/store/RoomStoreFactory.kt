package bloomy.cozyspace.store

import bloomy.cozyspace.cache.RoomCache
import bloomy.cozyspace.cache.RoomStorage
import bloomy.cozyspace.data.RoomRepository
import bloomy.cozyspace.data.dto.toDomain
import bloomy.cozyspace.domain.Room
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class RoomStoreFactory(
    private val repository: RoomRepository,
    private val storage: RoomStorage,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): RoomStore {
        val cache = storage.get()

        val initialState = RoomStore.State(
            rooms = cache?.rooms ?: emptyList(),
        )

        return object : RoomStore,
            Store<RoomStore.Intent, RoomStore.State, RoomStore.Label> by storeFactory.create(
                name = "RoomStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class GetRoomsSuccess(val rooms: List<Room>) : Msg
        data class Error(val message: String) : Msg
        data object Clear : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
            RoomStore.Intent,
            Unit,
            RoomStore.State,
            Msg,
            RoomStore.Label,
            >() {

        override fun executeIntent(intent: RoomStore.Intent) {
            when (intent) {
                is RoomStore.Intent.GetRooms -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getRooms(intent.houseId)) {
                            is ApiResult.Success -> {
                                val rooms = result.data.map { it.toDomain() }

                                if (!intent.saveMode) storage.save(RoomCache(rooms))

                                dispatch(Msg.GetRoomsSuccess(rooms))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(RoomStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(RoomStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }
                RoomStore.Intent.Clear -> dispatch(Msg.Clear)
            }
        }
    }

    private object ReducerImpl : Reducer<RoomStore.State, Msg> {
        override fun RoomStore.State.reduce(msg: Msg): RoomStore.State {
            return when (msg) {
                is Msg.Loading -> copy(
                    loading = true,
                    error = null,
                )

                is Msg.GetRoomsSuccess -> copy(
                    loading = false,
                    rooms = msg.rooms,
                    error = null,
                )

                is Msg.Error -> copy(
                    loading = false,
                    error = msg.message,
                )

                is Msg.Clear -> RoomStore.State()
            }
        }
    }
}
