package bloomy.cozyspace.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.House
import bloomy.cozyspace.store.HouseStore
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.utils.observeState
import cozyspace.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.alpha
import bloomy.cozyspace.home.components.microComponents.SaveItem
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.utils.CustomVerticalScrollbar
import cozyspace.composeapp.generated.resources.add

@Composable
fun SavesPopup(
    stores: Stores,
    isOnline: Boolean,
    onViewHouse: (House) -> Unit = {},
    onAddClick: () -> Unit = {},
) {
    val houseState = stores.house.observeState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        stores.house.accept(HouseStore.Intent.GetHouse)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkGreen)
            .padding(20.dp),
    ) {
        Text(
            text = "Saves",
            color = WhiteBackground,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Box(modifier = Modifier.weight(1f, fill = false)) {
            when {
                houseState.loading && houseState.savedHouses.isEmpty() -> {
                    CircularProgressIndicator(
                        color = WhiteBackground,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                    )
                }

                houseState.savedHouses.isEmpty() -> {
                    Text(
                        text = "No saves at the moment",
                        color = WhiteBackground.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                    )
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                    ) {
                        items(houseState.savedHouses, key = { it.id }) { house ->
                            SaveItem(
                                house = house,
                                onViewClick = { onViewHouse(house) },
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FloatingActionButton(
            onClick = { if (isOnline) onAddClick() },
            containerColor = LightGreen,
            contentColor = WhiteBackground,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.Start)
                .size(48.dp)
                .alpha(if (isOnline) 1f else 0.4f),
        ) {
            Icon(
                painter = painterResource(Res.drawable.add),
                contentDescription = if (isOnline) "Add a save" else "Addition not available offline",
            )
        }
    }
}
