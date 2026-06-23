package bloomy.cozyspace.navigation

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.navigation.screenRoutes.Home
import bloomy.cozyspace.navigation.screenRoutes.NavDestination
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground

@Composable
fun MobileNavBar(
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

        val indicatorSize by animateDpAsState(
            targetValue = if (selectedIndex == Home.navIndex) 68.dp else 56.dp,
            animationSpec = spring(
                dampingRatio = 0.6f,
                stiffness = Spring.StiffnessLow,
            ),
            label = "Indicator Size"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    DarkGreen,
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
                    .size(indicatorSize)
                    .background(
                        WhiteBackground,
                        shape = CircleShape
                    )
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
                        DarkGreen else WhiteBackground,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = Spring.StiffnessLow,
                    ),
                )

                val iconSize by animateDpAsState(
                    targetValue = when {
                        item == Home && selectedIndex == index -> 36.dp
                        item == Home -> 32.dp
                        selectedIndex == index -> 28.dp
                        else -> 24.dp
                    },
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = Spring.StiffnessLow,
                    ),
                    label = "Icon Size"
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
                        painter = navIcon(item),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}
