package bloomy.cozyspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.navigation.NavGraph
import bloomy.cozyspace.store.HouseStore
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.store.RoomStore
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoDoneStore
import bloomy.cozyspace.store.TodoListStore
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.utils.*
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.cloud_off
import org.jetbrains.compose.resources.painterResource

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val env = rememberAppEnvironment(scope)

    val userStore = rememberUserStore(env).value
    val houseStore = rememberHouseStore(env).value
    val roomStore = rememberRoomStore(env).value
    val rewardStore = rememberRewardStore(env).value
    val todoListStore = rememberTodoListStore(env).value
    val todoDoneStore = rememberTodoDoneStore(env).value

    if (userStore == null || houseStore == null || roomStore == null || rewardStore == null || todoListStore == null || todoDoneStore == null) {
        LoadingScreen()
        return
    }

    val stores = Stores(
        user = userStore,
        reward = rewardStore,
        room = roomStore,
        house = houseStore,
        todoList = todoListStore,
        todoDone = todoDoneStore,
    )

    LaunchedEffect(stores.user) {
        env.forcedLogout.collect { stores.user.accept(UserStore.Intent.Logout) }
    }

    ObserveUserNavigation(stores, navController)
    ObserveTodoListEvents(stores)

    ObserveErrors(stores.user, snackbarHostState, scope) { (it as? UserStore.Label.ShowError)?.message }
    ObserveErrors(stores.house, snackbarHostState, scope) { (it as? HouseStore.Label.ShowError)?.message }
    ObserveErrors(stores.room, snackbarHostState, scope) { (it as? RoomStore.Label.ShowError)?.message }
    ObserveErrors(stores.reward, snackbarHostState, scope) { (it as? RewardStore.Label.ShowError)?.message }
    ObserveErrors(stores.todoList, snackbarHostState, scope) { (it as? TodoListStore.Label.ShowError)?.message }
    ObserveErrors(stores.todoDone, snackbarHostState, scope) { (it as? TodoDoneStore.Label.ShowError)?.message }

    val isOnline by env.networkMonitor.isOnline.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        NavGraph(
            navController = navController,
            stores = stores,
            storages = env.storages,
            isOnline = isOnline,
        )

        if (!isOnline) {
            Icon(
                painter = painterResource(Res.drawable.cloud_off),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .padding(6.dp)
                    .size(24.dp),
                tint = DarkGreen,
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
        )
    }
}
