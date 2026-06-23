package bloomy.cozyspace.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bloomy.cozyspace.home.HomeMain
import bloomy.cozyspace.navigation.screenRoutes.Home
import bloomy.cozyspace.navigation.screenRoutes.NavDestination
import bloomy.cozyspace.navigation.screenRoutes.Timers
import bloomy.cozyspace.navigation.screenRoutes.Todo
import bloomy.cozyspace.timers.TimersMain
import bloomy.cozyspace.todoList.TodoMain
import cozyspace.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun navIcon(destination: NavDestination): Painter {
    return when (destination) {
        Home -> painterResource(Res.drawable.home)
        Timers -> painterResource(Res.drawable.schedule)
        Todo -> painterResource(Res.drawable.select_check_box)
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Timers> {
            TimersMain()
        }

        composable<Home> {
            HomeMain()
        }

        composable<Todo> {
            TodoMain()
        }
    }
}
