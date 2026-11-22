package bloomy.cozyspace.timers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
    onNavbarVisibilityChange: (Boolean) -> Unit = {}
) {
    val navTimerController = rememberNavController()
    val backStackEntry by navTimerController.currentBackStackEntryAsState()
    val hiddenRoutes = listOf(
        TimerScreen.TimerRelaxationIntrospection.route,
        TimerScreen.TimerRelaxationBoxBreathing.route
    )

    val showNavbar = backStackEntry?.destination?.route !in hiddenRoutes

    LaunchedEffect(showNavbar) {
        onNavbarVisibilityChange(showNavbar)
    }

    DisposableEffect(Unit) {
        onDispose { onNavbarVisibilityChange(true) }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        TimerNavGraph(navTimerController, stores)
    }
}
