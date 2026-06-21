package bloomy.cozyspace

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.home
import cozyspace.composeapp.generated.resources.schedule
import cozyspace.composeapp.generated.resources.select_check_box

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Navigation Destinations
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
    override val navIndex: Int = 0
    override val navTitle: String = "Home"
}

@Serializable
data object Timers : NavDestination {
    override val navIndex: Int = 1
    override val navTitle: String = "Timers"
}

@Serializable
data object Todo : NavDestination {
    override val navIndex: Int = 2
    override val navTitle: String = "Todo"
}

@Composable
fun navIcon(destination: NavDestination): Painter {
    return when (destination) {
        Home -> painterResource(Res.drawable.home)
        Timers -> painterResource(Res.drawable.schedule)
        Todo -> painterResource(Res.drawable.select_check_box)
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScreen() {

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
            composable<Home> {
                HomeTabScreen()
            }

            composable<Timers> {
                TimersTabScreen()
            }

            composable<Todo> {
                TodoTabScreen()
            }
        }
    }
}

@Composable
fun NavBar(
    items: List<NavDestination>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(80.dp),
    ) {
        val itemWidth = maxWidth / items.size

        // main logic
        val indicatorOffset by animateDpAsState(
            targetValue = itemWidth * selectedIndex,
            animationSpec = spring(
                dampingRatio = 0.6f,
                stiffness = Spring.StiffnessLow,
            ),
            label = "Indicator Offset",
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ),
        )

        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(itemWidth)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = CircleShape),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val iconTint by animateColorAsState(
                    targetValue = if (selectedIndex == index)
                        MaterialTheme.colorScheme.primary else Color.White,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = Spring.StiffnessLow,
                    ),
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(interactionSource = remember {
                            MutableInteractionSource()
                        }) {
                            onItemSelected(index)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = navIcon(NavDestination.entries[index]),
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            }
        }
    }
}

// Tab Screen
@Composable
private fun HomeTabScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Home Content")
    }
}

@Composable
private fun TimersTabScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Timers Content")
    }
}

@Composable
private fun TodoTabScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Todo Content")
    }
}
