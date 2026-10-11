package bloomy.cozyspace.timers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.navigation.timerRoutes.TimerNavGraph
import bloomy.cozyspace.navigation.timerRoutes.TimerScreen
import bloomy.cozyspace.store.Stores

@Composable
fun TimersMain(
    stores: Stores,
    onActiveTimerPageChanged: (Boolean) -> Unit,
) {
    val navTimerController = rememberNavController()
    val currentBackStackEntry by navTimerController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isActiveTimerPage =
        currentRoute != null && currentRoute in TimerScreen.activeTimerRoutes

    DisposableEffect(isActiveTimerPage) {
        onActiveTimerPageChanged(isActiveTimerPage)
        onDispose {
            if (isActiveTimerPage) onActiveTimerPageChanged(false)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        TimerNavGraph(navTimerController, stores)
    }
}