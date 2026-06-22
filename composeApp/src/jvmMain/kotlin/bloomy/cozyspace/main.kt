package bloomy.cozyspace

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.cozyspace_logo

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "CozySpace",
            icon = painterResource(Res.drawable.cozyspace_logo)
        ) {
            App()
        }
    }
