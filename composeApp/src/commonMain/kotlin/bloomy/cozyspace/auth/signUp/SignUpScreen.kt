package bloomy.cozyspace.auth.signUp

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.auth.common.AuthHeader
import bloomy.cozyspace.auth.common.AuthLayout
import bloomy.cozyspace.auth.signIn.SignInForm

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit = {}, onSignInSuccess: () -> Unit = {}) {
    BoxWithConstraints {
        AuthLayout(
            isCompact = maxWidth < 600.dp,
            isSignUp = true,
            headerWeight = 0.4f,
            header = {
                AuthHeader(
                    title = "Welcome to CozySpace !",
                    imageWidth = maxWidth * 0.4f
                )
            },
            content = {
                SignUpForm(
                    onSignUpSuccess = onSignUpSuccess,
                    onSignInSuccess = onSignInSuccess
                )
            }
        )
    }
}
