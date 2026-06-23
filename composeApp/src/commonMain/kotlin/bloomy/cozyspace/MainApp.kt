package bloomy.cozyspace

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.home.HomeMain
import bloomy.cozyspace.navigation.NavBar
import bloomy.cozyspace.navigation.screenRoutes.Home
import bloomy.cozyspace.navigation.screenRoutes.Timers
import bloomy.cozyspace.navigation.screenRoutes.Todo
import bloomy.cozyspace.navigation.screenRoutes.NavDestination
import bloomy.cozyspace.timers.TimersMain
import bloomy.cozyspace.todoList.TodoMain

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Preview
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentDestination = NavDestination.fromRoute(currentRoute)

    val selectedIndex = currentDestination.navIndex

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "CozySpace",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified,
                ),
            )
        },
        bottomBar = {
            NavBar(
                items = NavDestination.entries.map { it },
                selectedIndex = selectedIndex,
                onItemSelected = {
                    val destination = NavDestination.fromIndex(it)
                    navController.navigate(destination) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(padding),
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
}
