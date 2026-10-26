package bloomy.cozyspace.store

import bloomy.cozyspace.cache.TimerMusicCache
import bloomy.cozyspace.cache.TimerMusicStorage
import bloomy.cozyspace.data.TimerMusicRepository
import bloomy.cozyspace.data.dto.toDomain
import bloomy.cozyspace.domain.TimerMusic
import bloomy.cozyspace.interfaces.ApiResult
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.launch

class TimerMusicStoreFactory(
    private val repository: TimerMusicRepository,
    private val storage: TimerMusicStorage,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    suspend fun create(): TimerMusicStore {
        val cache = storage.get()

        val initialState = TimerMusicStore.State(
            timersMusics = cache?.timersMusics ?: emptyList(),
        )

        return object : TimerMusicStore,
            Store<TimerMusicStore.Intent, TimerMusicStore.State, TimerMusicStore.Label> by storeFactory.create(
                name = "TimerMusicStore",
                initialState = initialState,
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Msg {
        data object Loading : Msg
        data object Offline : Msg
        data class GetTimersMusicSuccess(val timersMusics: List<TimerMusic>) : Msg
        data class Error(val message: String) : Msg
        data object Clear : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<
        TimerMusicStore.Intent,
        Unit,
        TimerMusicStore.State,
        TimerMusicStoreFactory.Msg,
        TimerMusicStore.Label,
        >() {

        override fun executeIntent(intent: TimerMusicStore.Intent) {
            when (intent) {
                TimerMusicStore.Intent.GetTimersMusics -> {
                    dispatch(Msg.Loading)

                    scope.launch {
                        when (val result = repository.getTimersMusics()) {
                            is ApiResult.Success -> {
                                val timersMusics = result.data.map { it.toDomain() }

                                storage.save(
                                    TimerMusicCache(
                                        timersMusics = timersMusics,
                                    ),
                                )

                                dispatch(Msg.GetTimersMusicSuccess(timersMusics))
                            }

                            is ApiResult.Error -> {
                                dispatch(Msg.Error(result.message))
                                publish(TimerMusicStore.Label.ShowError(result.message))
                            }

                            ApiResult.Offline -> dispatch(Msg.Offline)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<TimerMusicStore.State, TimerMusicStoreFactory.Msg> {
        override fun TimerMusicStore.State.reduce(msg: TimerMusicStoreFactory.Msg): TimerMusicStore.State {
            return when (msg) {
                Msg.Loading -> copy(
                    loading = true,
                    error = null,
                )

                Msg.Offline -> copy(
                    loading = false,
                    error = null,
                )

                is Msg.GetTimersMusicSuccess -> copy(
                    loading = false,
                    timersMusics = msg.timersMusics,
                    error = null,
                )

                is Msg.Error -> copy(
                    loading = false,
                    error = msg.message,
                )

                Msg.Clear -> TimerMusicStore.State()
            }
        }
    }
}
