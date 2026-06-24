package bloomy.cozyspace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import bloomy.cozyspace.navigation.AppNavHost
import bloomy.cozyspace.navigation.DesktopNavBar
import bloomy.cozyspace.navigation.MobileNavBar
import bloomy.cozyspace.navigation.screenRoutes.NavDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileLayout(
    navController: NavHostController,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("CozySpace")
                }
            )
        },
        bottomBar = {
            MobileNavBar(
                items = NavDestination.entries,
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected
            )
        }
    ) { padding ->

        AppNavHost(
            navController,
            Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopLayout(
    navController: NavHostController,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(
            modifier = Modifier.weight(1f),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("CozySpace")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->

            AppNavHost(
                navController,
                Modifier.padding(padding)
            )
        }

        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            DesktopNavBar(
                items = NavDestination.entries,
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected
            )
        }
    }
}
