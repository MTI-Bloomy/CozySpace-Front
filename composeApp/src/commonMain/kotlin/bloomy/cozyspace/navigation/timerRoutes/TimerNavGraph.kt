package bloomy.cozyspace.navigation.timerRoutes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.timers.common.TimerEndSessionScreen
import bloomy.cozyspace.timers.timerConcentration.TimerConcentrationScreen
import bloomy.cozyspace.timers.timerMain.TimerMainScreen
import bloomy.cozyspace.timers.timerRelaxation.TimerRelaxationIntrospectionScreen
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
                fromTimerRelaxation_toTimerRelaxationIntrospection = {
                    navController.navigate(TimerScreen.TimerRelaxationIntrospection.route)
                },
                fromTimerRelaxation_toTimerRelaxationSquareBreathing = {
                    navController.navigate(TimerScreen.TimerRelaxationSquareBreathing.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(TimerScreen.TimerRelaxationIntrospection.route) {
            TimerRelaxationIntrospectionScreen(
                onBack = { navController.popBackStack() },
                onFinish = {
                    navController.navigate(TimerScreen.TimerEndSession.createRoute(isConcentration = false))
                }
            )
        }

        composable(TimerScreen.TimerRelaxationSquareBreathing.route) {
//            TimerRelaxationSquareBreathingScreen(
//            onBack = { navController.popBackStack() },
//            onFinish = {
//                navController.navigate(TimerScreen.TimerEndSession.createRoute(isConcentration = true))
//            }
//         )
        }

        composable(
            route = TimerScreen.TimerEndSession.route,
            arguments = listOf(
                navArgument(TimerScreen.TimerEndSession.ARG) { type = NavType.BoolType }
            )
        ) { entry ->
            val isConcentration = entry.arguments?.read {
                if (contains(TimerScreen.TimerEndSession.ARG)) getBoolean(TimerScreen.TimerEndSession.ARG) else false
            } ?: false

            TimerEndSessionScreen(
                isConcentration = isConcentration,
                onRestart = {
                    val target = if (isConcentration) TimerScreen.TimerConcentration.route
                    else TimerScreen.TimerRelaxation.route
                    navController.popBackStack(target, inclusive = false)
                },
                onHome = {
                    navController.popBackStack(TimerScreen.TimerMain.route, inclusive = false)
                }
            )
        }
    }
}
