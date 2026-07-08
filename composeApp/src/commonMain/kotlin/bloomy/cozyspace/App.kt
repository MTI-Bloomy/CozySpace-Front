package bloomy.cozyspace

// import androidx.compose.foundation.background
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.Spacer
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.safeContentPadding
// import androidx.compose.material3.Button
// import androidx.compose.material3.MaterialTheme
// import androidx.compose.material3.Text
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.DisposableEffect
// import androidx.compose.runtime.collectAsState
// import androidx.compose.runtime.getValue
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.rememberCoroutineScope
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
// import androidx.compose.ui.tooling.preview.Preview
// import bloomy.cozyspace.data.AuthentificationRepository
// import bloomy.cozyspace.data.LoginRequestDto
// import bloomy.cozyspace.data.RegisterRequestDto
// import bloomy.cozyspace.network.ApiService
// import bloomy.cozyspace.network.createHttpClient
// import bloomy.cozyspace.store.UserStore
// import bloomy.cozyspace.store.UserStoreFactory
// import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.cache.createUserStorage
import bloomy.cozyspace.data.AuthentificationRepository
import bloomy.cozyspace.navigation.NavGraph
import bloomy.cozyspace.navigation.screenRoutes.Screen
import bloomy.cozyspace.network.ApiService
import bloomy.cozyspace.network.createHttpClient
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.store.UserStoreFactory
import bloomy.cozyspace.utils.LoadingScreen
import com.arkivanov.mvikotlin.core.rx.observer
import kotlinx.coroutines.launch

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun App() {
/*     MaterialTheme {
        val userStore =
            remember {
                UserStoreFactory(
                    AuthentificationRepository(
                        ApiService(
                            createHttpClient()
                        )
                    )
                ).create().also { it.init() }
            }
        val scope = rememberCoroutineScope()
        val userFlow = remember(userStore, scope) { userStore.stateFlow(scope) }
        val user by userFlow.collectAsState()

        DisposableEffect(userStore) {
            onDispose {
                userStore.dispose()
            }
        }

        Column(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    userStore.accept(UserStore.Intent.Register(
                        RegisterRequestDto(
                            email = "user@example.com",
                            password = "password"
                        )
                    ))
                }
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    userStore.accept(UserStore.Intent.Login(
                        LoginRequestDto(
                            email = "user@example.com",
                            password = "password"
                        )
                    ))
                }
            ) {
                Text("Login")
            }

            Text(text = if (user.loading) "Loading..." else user.token.idToken)

            user.error?.let { error ->
                Text(text = error)
            }
        }
    } */
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val userStore by produceState<UserStore?>(initialValue = null) {
        value = UserStoreFactory(
            repository = AuthentificationRepository(
                ApiService(createHttpClient())
            ),
            storage = createUserStorage()
        ).create().also { it.init() }
    }

    if (userStore == null) {
        LoadingScreen()
        return
    }

    val uStore = userStore!!

    val stores = Stores(
        uStore
    )

    val scope = rememberCoroutineScope()

    DisposableEffect(userStore) {

        val disposable = stores.user.labels(
            observer { label ->
                when (label) {

                    is UserStore.Label.ShowError -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(label.message)
                        }
                    }

                    UserStore.Label.Logout -> {
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                    UserStore.Label.LoginSuccess -> {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    }

                    UserStore.Label.RegisterSuccess -> {
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    }
                }
            }
        )

        onDispose {
            disposable.dispose()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.systemBars)
    ) {

        NavGraph(
            navController = navController,
            stores = stores
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}
