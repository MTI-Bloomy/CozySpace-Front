package bloomy.cozyspace.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.store.RoomStore
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoDoneStore
import com.arkivanov.mvikotlin.core.rx.observer

@Composable
fun ObserveRewardsEvent(stores: Stores) {
    DisposableEffect(stores.reward) {
        val disposable = stores.reward.labels(
            observer { label ->
                when (label) {
                    is RewardStore.Label.RefreshTodoDoneNotClaimed -> {
                        stores.todoDone.accept(TodoDoneStore.Intent.GetNotClaimedTodoDone)
                        stores.house.state.house?.id?.let { houseId ->
                            stores.room.accept(RoomStore.Intent.GetRooms(houseId))
                        }
                    }

                    else -> Unit
                }
            },
        )
        onDispose { disposable.dispose() }
    }
}
