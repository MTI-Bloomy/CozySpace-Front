package bloomy.cozyspace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bloomy.cozyspace.auth.signIn.SignInScreen
import bloomy.cozyspace.auth.signUp.SignUpScreen
import bloomy.cozyspace.navigation.screenRoutes.Screen

@Composable
fun NavGraph (navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(navController)
        }

        composable(Screen.CreateAccount.route) {
            SignUpScreen(navController)
        }
    }
}


