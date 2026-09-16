package bloomy.cozyspace.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import bloomy.cozyspace.cache.SyncQueue
import bloomy.cozyspace.navigation.screenRoutes.Screen
import bloomy.cozyspace.store.HouseStore
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.store.RoomStore
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoDoneStore
import bloomy.cozyspace.store.TodoListStore
import bloomy.cozyspace.store.UserStore
import com.arkivanov.mvikotlin.core.rx.observer

@Composable
fun ObserveUserNavigation(stores: Stores, navController: NavController, syncQueue: SyncQueue) {
    DisposableEffect(stores.user) {
        val disposable = stores.user.labels(
            observer { label ->
                when (label) {
                    UserStore.Label.Logout -> {
                        stores.house.accept(HouseStore.Intent.Clear)
                        stores.room.accept(RoomStore.Intent.Clear)
                        stores.reward.accept(RewardStore.Intent.Clear)
                        stores.todoList.accept(TodoListStore.Intent.Clear)
                        stores.todoDone.accept(TodoDoneStore.Intent.Clear)

                        syncQueue.clear()

                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    UserStore.Label.LoginSuccess -> navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }

                    else -> Unit
                }
            },
        )
        onDispose { disposable.dispose() }
    }
}
