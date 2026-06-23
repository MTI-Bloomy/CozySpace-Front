package bloomy.cozyspace.auth.signIn

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import bloomy.cozyspace.auth.common.AuthHeader
import bloomy.cozyspace.auth.common.AuthLayout
import bloomy.cozyspace.store.UserStore

@Composable
fun SignInScreen(navController: NavHostController, userStore: UserStore) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isCompact = maxWidth < 600.dp
        val density = LocalDensity.current
        val keyboardOpen = WindowInsets.ime.getBottom(density) > 0

        AuthLayout(
            isCompact = isCompact,
            keyboardOpen = keyboardOpen,
            header = {
                AuthHeader(
                    title = "Welcome back !",
                    imageWidth = if (keyboardOpen) {
                        maxWidth * 0.22f
                    } else {
                        maxWidth * 0.45f
                    }
                )
            },
            content = {
                SignInForm(navController, userStore)
            }
        )
    }
}
