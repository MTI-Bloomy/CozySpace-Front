package bloomy.cozyspace.navigation.screenRoutes

sealed class Screen(val route: String) {
    data object SignIn : Screen("signin")
    data object CreateAccount : Screen("create_account")
    data object Main : Screen("main")
}
