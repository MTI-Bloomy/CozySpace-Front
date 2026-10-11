package bloomy.cozyspace.navigation.timerRoutes

sealed class TimerScreen(val route: String) {
    data object TimerMain : TimerScreen("timer_main")
    data object TimerConcentration : TimerScreen("timer_concentration")
    data object TimerRelaxation : TimerScreen("timer_relaxation")
    data object TimerRelaxationIntrospection : TimerScreen("timer_relaxation_introspection")
    data object TimerRelaxationSquareBreathing : TimerScreen("timer_relaxation_square_breathing")
}
