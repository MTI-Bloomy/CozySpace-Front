package bloomy.cozyspace.navigation.timerRoutes

sealed class TimerScreen(val route: String) {
    data object TimerMain : TimerScreen("timer_main")
    data object TimerConcentration : TimerScreen("timer_concentration")
    data object TimerClassic : TimerScreen("timer_classic")
    data object TimerRelaxation : TimerScreen("timer_relaxation")

    companion object {
        // Selection and end screens stay outside this set so the navbar remains visible there.
        val activeTimerRoutes = setOf(TimerClassic.route)
    }
}
