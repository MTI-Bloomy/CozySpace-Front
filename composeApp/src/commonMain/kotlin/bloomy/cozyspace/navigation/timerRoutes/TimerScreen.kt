package bloomy.cozyspace.navigation.timerRoutes

sealed class TimerScreen(val route: String) {
    data object TimerMain : TimerScreen("timer_main")
    data object TimerConcentration : TimerScreen("timer_concentration")
    data object TimerRelaxation : TimerScreen("timer_relaxation")
}
