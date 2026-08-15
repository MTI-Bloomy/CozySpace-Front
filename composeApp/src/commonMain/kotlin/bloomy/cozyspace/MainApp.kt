package bloomy.cozyspace

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.navigation.screenRoutes.NavDestination
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.utils.DesktopLayout
import bloomy.cozyspace.utils.MobileLayout

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainApp(stores: Stores, storages: Storages) {
    BoxWithConstraints {

        val isLargeScreen = maxWidth > 600.dp

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val currentDestination = NavDestination.fromRoute(currentRoute)
        val selectedIndex = currentDestination.navIndex

        val onItemSelected: (Int) -> Unit = {
            val destination = NavDestination.fromIndex(it)

            navController.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        if (isLargeScreen) {
            DesktopLayout(
                navController = navController,
                stores = stores,
                storages = storages,
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected
            )
        } else {
            MobileLayout(
                navController = navController,
                stores = stores,
                storages = storages,
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected
            )
        }
    }
}
