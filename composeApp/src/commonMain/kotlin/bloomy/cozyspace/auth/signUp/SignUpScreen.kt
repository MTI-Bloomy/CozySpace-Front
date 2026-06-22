package bloomy.cozyspace.auth.signUp

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

@Composable
fun SignUpScreen(navController: NavHostController) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isCompact = maxWidth < 600.dp
        val density = LocalDensity.current
        val keyboardOpen = WindowInsets.ime.getBottom(density) > 0

        AuthLayout(
            isCompact = isCompact,
            keyboardOpen = keyboardOpen,
            isSignUp = true,
            header = {
                AuthHeader(
                    title = "Welcome to CozySpace !",
                    imageWidth = if (keyboardOpen) {
                        maxWidth * 0.18f
                    } else {
                        maxWidth * 0.40f
                    }
                )
            },
            content = {
                SignUpForm(navController)
            }
        )
    }
}
