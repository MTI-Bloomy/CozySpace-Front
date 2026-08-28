package bloomy.cozyspace.store

import bloomy.cozyspace.data.HouseRepository
import bloomy.cozyspace.data.dto.createHouseRequestDto
import bloomy.cozyspace.data.dto.toDomain
import bloomy.cozyspace.domain.House
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class HouseStoreFactory(
    private val repository: HouseRepository,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): HouseStore {
        val initialState = HouseStore.State(
            savedHouses = emptyList(),
        )

        return object : HouseStore,
            Store<HouseStore.Intent, HouseStore.State, HouseStore.Label> by storeFactory.create(
                name = "HouseStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class GetHouseSuccess(val house: List<House>) : Msg
        data class CreateHouseSuccess(val house: House) : Msg
        data class Error(val message: String) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
        HouseStore.Intent,
        Unit,
        HouseStore.State,
        Msg,
        HouseStore.Label,
        >() {

        override fun executeIntent(intent: HouseStore.Intent) {
            when (intent) {
                HouseStore.Intent.GetHouse -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getHouse()) {
                            is ApiResult.Success -> {
                                if (result.data.isEmpty()) {
                                    createHouse(createHouseRequestDto(name = "Default"))
                                } else {
                                    dispatch(Msg.GetHouseSuccess(result.data.map { it.toDomain() }))
                                }
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(HouseStore.Label.ShowError(result.message))
                            }

                            ApiResult.Empty -> {
                                dispatch(Msg.Error("Empty response from server"))
                                publish(HouseStore.Label.ShowError("Empty response from server"))
                            }
                        }
                    }
                }

                is HouseStore.Intent.CreateHouse -> {
                    dispatch(Msg.Loading)
                    scope.launch { createHouse(createHouseRequestDto(intent.name)) }
                }
            }
        }

        private suspend fun createHouse(request: createHouseRequestDto) {
            when (val result = repository.createHouse(request)) {
                is ApiResult.Success -> {
                    dispatch(Msg.CreateHouseSuccess(result.data.toDomain()))
                }

                is ApiResult.Error -> {
                    dispatch(Msg.Error(result.message))
                    publish(HouseStore.Label.ShowError(result.message))
                }

                ApiResult.Empty -> {
                    dispatch(Msg.Error("Empty response from server"))
                    publish(HouseStore.Label.ShowError("Empty response from server"))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<HouseStore.State, Msg> {
        override fun HouseStore.State.reduce(msg: Msg): HouseStore.State {
            return when (msg) {
                is Msg.Loading -> copy(
                    loading = true,
                    error = null,
                )

                is Msg.GetHouseSuccess -> copy(
                    loading = false,
                    house = msg.house.find { it.saveDate == null },
                    savedHouses = msg.house.filter { it.saveDate != null },
                    error = null,
                )

                is Msg.CreateHouseSuccess -> copy(
                    loading = false,
                    house = msg.house,
                    error = null,
                )

                is Msg.Error -> copy(
                    loading = false,
                    error = msg.message,
                )
            }
        }
    }
}
