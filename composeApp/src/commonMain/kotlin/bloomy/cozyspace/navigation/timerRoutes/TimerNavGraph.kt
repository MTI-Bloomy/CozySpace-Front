package bloomy.cozyspace.navigation.timerRoutes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.timers.timerConcentration.TimerConcentrationScreen
import bloomy.cozyspace.timers.timerMain.TimerMainScreen
import bloomy.cozyspace.timers.timerRelaxation.TimerRelaxationScreen

@Composable
fun TimerNavGraph (navController: NavHostController, stores: Stores) {

    NavHost(
        navController = navController,
        startDestination = TimerScreen.TimerMain.route
    ) {
        composable(TimerScreen.TimerMain.route) {
            TimerMainScreen(
                fromTimerMain_toTimerConcentration = {
                    navController.navigate(TimerScreen.TimerConcentration.route)
                },
                fromTimerMain_toTimerRelaxation = {
                    navController.navigate(TimerScreen.TimerRelaxation.route)
                },
                stores
            )
        }

        composable(TimerScreen.TimerConcentration.route) {
            TimerConcentrationScreen(
                stores
            )
        }

        composable(TimerScreen.TimerRelaxation.route) {
            TimerRelaxationScreen(
                stores,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
