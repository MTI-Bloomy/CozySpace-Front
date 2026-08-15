package bloomy.cozyspace.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.utils.observeState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.home.components.RoomView
import bloomy.cozyspace.store.HouseStore
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.store.RoomStore
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.utils.LoadingScreen
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.logout
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeMain(stores: Stores, storages: Storages) {
    val houseState = stores.house.observeState()
    val roomState = stores.room.observeState()
    val rewardState = stores.reward.observeState()

    LaunchedEffect(Unit) {
        stores.house.accept(HouseStore.Intent.GetHouse)
        stores.reward.accept(RewardStore.Intent.GetRewards)
    }

    if (houseState.house != null) {
        LaunchedEffect(Unit) {
            stores.room.accept(RoomStore.Intent.GetRooms(houseState.house!!.id))
        }
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen,
                    ),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.logout),
                        contentDescription = "",
                        tint = WhiteBackground,
                    )
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(WhiteBackground).padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            // FIXME: when switch room feature implemented, use id of current room to find them instead of hardcode type
            if (roomState.rooms.isNotEmpty()) {
                RoomView(
                    rewards = rewardState.rewards.filter { reward ->
                        roomState.rooms.filter { room -> room.type == RoomType.KITCHEN }[0].furniture.contains(
                            reward.id,
                        )
                    },
                    storages = storages,
                )
            } else {
                LoadingScreen()
            }
        }
    }
}
