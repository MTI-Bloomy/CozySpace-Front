package bloomy.cozyspace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bloomy.cozyspace.login.signIn.SignInScreen
import bloomy.cozyspace.login.signUp.SignUpScreen
import bloomy.cozyspace.navigation.screenRoutes.Screen

@Composable
fun NavGraph (navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.SignIn.route)
                    // TODO => should lead to main screen
                },
                onSignUp = {
                    navController.navigate(Screen.CreateAccount.route)
                }
            )
        }

        composable(Screen.CreateAccount.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.SignIn.route)
                    // TODO => verify user successfully created before changing screen
                },
                onSignInSuccess = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }
    }
}


