package bloomy.cozyspace.navigation.screenRoutes

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavDestination {
    val navIndex: Int
    val navTitle: String

    companion object {
        val entries: List<NavDestination> = listOf(Timers, Home, Todo)
        fun fromIndex(targetIndex: Int): NavDestination = entries.find { it.navIndex == targetIndex } ?: Home
        fun fromRoute(route: String?): NavDestination =
            entries.find { route?.contains(it::class.simpleName ?: "") == true } ?: Home
    }
}

@Serializable
data object Home : NavDestination {
    override val navIndex: Int = 1
    override val navTitle: String = "Home"
}

@Serializable
data object Timers : NavDestination {
    override val navIndex: Int = 0
    override val navTitle: String = "Timers"
}

@Serializable
data object Todo : NavDestination {
    override val navIndex: Int = 2
    override val navTitle: String = "Todo"
}
