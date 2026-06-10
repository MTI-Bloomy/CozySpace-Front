package bloomy.cozyspace.auth.signIn

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.auth.common.AuthHeader
import bloomy.cozyspace.auth.common.AuthLayout

@Composable
fun SignInScreen(onLoginSuccess: () -> Unit = {}, onSignUp: () -> Unit = {}) {
    BoxWithConstraints {
        AuthLayout(
            isCompact = maxWidth < 600.dp,
            header = {
                AuthHeader(
                    title = "Welcome back !",
                    imageWidth = maxWidth * 0.4f
                )
            },
            content = {
                SignInForm(
                    onLoginSuccess = onLoginSuccess,
                    onSignUp = onSignUp
                )
            }
        )
    }
}
