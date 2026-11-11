package bloomy.cozyspace.navigation.timerRoutes

sealed class TimerScreen(val route: String) {
    data object TimerMain : TimerScreen("timer_main")
    data object TimerConcentration : TimerScreen("timer_concentration")
    data object TimerRelaxation : TimerScreen("timer_relaxation")
    data object TimerRelaxationIntrospection : TimerScreen("timer_relaxation_introspection")
    data object TimerRelaxationBoxBreathing : TimerScreen("timer_relaxation_box_breathing")
    data object TimerEndSession : TimerScreen("timer_end_session/{isConcentration}") {
        const val ARG = "isConcentration"
        fun createRoute(isConcentration: Boolean) = "timer_end_session/$isConcentration"
    }
}
