package bloomy.cozyspace.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bloomy.cozyspace.MainApp
import bloomy.cozyspace.auth.signIn.SignInScreen
import bloomy.cozyspace.auth.signUp.SignUpScreen
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.navigation.screenRoutes.Screen
import bloomy.cozyspace.store.Stores

@Composable
fun NavGraph (navController: NavHostController, stores: Stores, storages: Storages) {

    val startDestination = if(stores.user.state.token.idToken == "") Screen.SignIn.route else Screen.Main.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                navController = navController,
                stores = stores,
            )
        }

        composable(Screen.CreateAccount.route) {
            SignUpScreen(
                navController = navController,
                stores = stores
            )
        }

        composable(Screen.Main.route) {
            MainApp(
                stores = stores,
                storages = storages
            )
        }
    }
}


