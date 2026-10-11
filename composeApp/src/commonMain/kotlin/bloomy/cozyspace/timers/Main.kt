package bloomy.cozyspace.timers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.navigation.timerRoutes.TimerNavGraph
import bloomy.cozyspace.store.Stores

@Composable
fun TimersMain(stores: Stores) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val navTimerController = rememberNavController()
        TimerNavGraph(navTimerController, stores)
    }
}
