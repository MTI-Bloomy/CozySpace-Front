package bloomy.cozyspace.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import bloomy.cozyspace.domain.Reward
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.kitchen_base
import org.jetbrains.compose.resources.painterResource

expect fun ByteArray.toImageBitmap(): ImageBitmap

@Composable
fun RoomView(rewards: List<Reward>, images: Map<String, ByteArray?>) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Fond de la pièce
        Image(
            painter = painterResource(Res.drawable.kitchen_base),
            contentDescription = "Room base",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Meubles superposés
        rewards.forEach { reward ->
            val bytes = images[reward.id]
            if (bytes != null) {
                val bitmap = remember(bytes) { bytes.toImageBitmap() }
                Image(
                    bitmap = bitmap,
                    contentDescription = reward.furnitureId,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
