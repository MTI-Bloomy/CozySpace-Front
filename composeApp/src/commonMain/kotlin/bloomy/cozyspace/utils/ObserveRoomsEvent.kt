package bloomy.cozyspace.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.store.RoomStore
import bloomy.cozyspace.store.Stores
import com.arkivanov.mvikotlin.core.rx.observer

@Composable
fun ObserveRoomsEvent(stores: Stores) {
    DisposableEffect(stores.room) {
        val disposable = stores.room.labels(
            observer { label ->
                when (label) {
                    is RoomStore.Label.RefreshRoom -> {
                        label.rewards.forEach {
                            stores.reward.accept(RewardStore.Intent.GetReward(it))
                        }
                    }

                    else -> Unit
                }
            },
        )
        onDispose { disposable.dispose() }
    }
}
