package bloomy.cozyspace.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.utils.observeState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.home.components.RoomView
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.theme.WhiteBackground

@Composable
fun HomeMain(stores: Stores) {
    val rewardState = stores.reward.observeState()

    LaunchedEffect(Unit) {
        stores.reward.accept(RewardStore.Intent.GetRewards)
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Button(
                    onClick = {
                        stores.user.accept(UserStore.Intent.Logout)
                    },
                ) {
                    Text("Log out")
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(WhiteBackground).padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            RoomView(
                rewards = rewardState.rewards,
                images = rewardState.images
            )
        }
    }
}
