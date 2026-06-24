package bloomy.cozyspace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bloomy.cozyspace.MainApp
import bloomy.cozyspace.auth.signIn.SignInScreen
import bloomy.cozyspace.auth.signUp.SignUpScreen
import bloomy.cozyspace.navigation.screenRoutes.Screen
import bloomy.cozyspace.store.UserStore

@Composable
fun NavGraph (navController: NavHostController, userStore: UserStore) {

    val startDestination = if(userStore.state.token.idToken == "") Screen.SignIn.route else Screen.Main.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(navController, userStore)
        }

        composable(Screen.CreateAccount.route) {
            SignUpScreen(navController, userStore)
        }

        composable(Screen.Main.route) {
            MainApp()
        }
    }
}


