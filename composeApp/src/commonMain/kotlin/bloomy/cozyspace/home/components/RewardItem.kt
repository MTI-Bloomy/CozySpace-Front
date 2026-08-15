package bloomy.cozyspace.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.domain.Reward
import coil3.compose.AsyncImage
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.kitchen_base
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun RoomView(rewards: List<Reward>, storages: Storages) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Room Background
        Image(
            painter = painterResource(Res.drawable.kitchen_base),
            contentDescription = "Room base",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Stacked furniture
        rewards.forEach { reward ->
            val exists by produceState(initialValue = false, key1 = reward.furnitureId) {
                var timeout = 60.seconds
                while (timeout > 0.seconds && !storages.assetStorage.exists(reward.furnitureId)) {
                    delay(1.seconds)
                    timeout -= 1.seconds
                }
                value = timeout > 0.seconds
            }
            if (exists) {
                AsyncImage(
                    model = storages.assetStorage.path(reward.furnitureId),
                    contentDescription = reward.furnitureId,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            } /* else {
                // TODO: Review this if necessary to display
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = DarkGreen)
            } */
        }
    }
}
