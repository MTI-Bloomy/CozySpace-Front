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

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.cache.UserCache
import bloomy.cozyspace.cache.createAssetStorage
import bloomy.cozyspace.cache.createUserStorage
import bloomy.cozyspace.config.Environment
import bloomy.cozyspace.data.AssetRepository
import bloomy.cozyspace.data.AuthentificationRepository
import bloomy.cozyspace.data.HouseRepository
import bloomy.cozyspace.data.RewardRepository
import bloomy.cozyspace.data.RoomRepository
import bloomy.cozyspace.data.TodoListRepository
import bloomy.cozyspace.data.TodoDoneRepository
import bloomy.cozyspace.domain.User
import bloomy.cozyspace.navigation.NavGraph
import bloomy.cozyspace.navigation.screenRoutes.Screen
import bloomy.cozyspace.network.ApiService
import bloomy.cozyspace.network.clearBearerCache
import bloomy.cozyspace.network.createHttpClient
import bloomy.cozyspace.store.HouseStore
import bloomy.cozyspace.store.HouseStoreFactory
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.store.RewardStoreFactory
import bloomy.cozyspace.store.RoomStore
import bloomy.cozyspace.store.RoomStoreFactory
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoListStore
import bloomy.cozyspace.store.TodoListStoreFactory
import bloomy.cozyspace.store.TodoDoneStore
import bloomy.cozyspace.store.TodoDoneStoreFactory
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.store.UserStoreFactory
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.utils.LoadingScreen
import com.arkivanov.mvikotlin.core.rx.observer
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
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

    val storage = remember { createUserStorage() }
    val assetStorage = remember { createAssetStorage() }
    val forcedLogout = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

    val httpClient = remember {
        createHttpClient(
            baseUrl = Environment.API_URL,
            loadTokens = {
                storage.get()?.token
                    ?.takeIf { it.idToken.isNotBlank() }
                    ?.let { BearerTokens(it.idToken, it.refreshToken) }
            },
            onTokensRefreshed = { newToken ->
                val cache = storage.get()
                storage.save(
                    UserCache(token = newToken, user = cache?.user ?: User("", "", "", null))
                )
            },
            onRefreshFailed = {
                storage.clear()
                forcedLogout.tryEmit(Unit)
            }
        )
    }

    val userStore by produceState<UserStore?>(initialValue = null) {
        value = UserStoreFactory(
            repository = AuthentificationRepository(ApiService(httpClient)),
            storage = storage,
            onAuthStateChanged = { httpClient.clearBearerCache() }
        ).create().also { it.init() }
    }

    val rewardStore by produceState<RewardStore?>(initialValue = null) {
        value = RewardStoreFactory(
            repository = RewardRepository(ApiService(httpClient)),
            assetRepository = AssetRepository(httpClient, assetStorage),
        ).create().also { it.init() }
    }

    val roomStore by produceState<RoomStore?>(initialValue = null) {
        value = RoomStoreFactory(
            repository = RoomRepository(ApiService(httpClient)),
        ).create().also { it.init() }
    }

    val houseStore by produceState<HouseStore?>(initialValue = null) {
        value = HouseStoreFactory(
            repository = HouseRepository(ApiService(httpClient)),
        ).create().also { it.init() }
    }

    val todoListStore by produceState<TodoListStore?>(initialValue = null) {
        value = TodoListStoreFactory(
            repository = TodoListRepository(ApiService(httpClient)),
        ).create().also { it.init() }
    }

    val todoDoneStore by produceState<TodoDoneStore?>(initialValue = null) {
        value = TodoDoneStoreFactory(
            repository = TodoDoneRepository(ApiService(httpClient)),
        ).create().also { it.init() }
    }

    if (
        userStore == null ||
        rewardStore == null ||
        roomStore == null ||
        houseStore == null ||
        todoListStore == null ||
        todoDoneStore == null
    ) {
        LoadingScreen()
        return
    }

    val uStore = userStore!!

    val stores = Stores(
        uStore,
        rewardStore!!,
        roomStore!!,
        houseStore!!,
        todoListStore!!,
        todoDoneStore!!
    )

    LaunchedEffect(uStore) {
        forcedLogout.collect {
            uStore.accept(UserStore.Intent.Logout)
        }
    }

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
        .background(WhiteBackground)
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
