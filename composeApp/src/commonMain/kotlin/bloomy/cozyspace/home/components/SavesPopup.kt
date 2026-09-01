package bloomy.cozyspace.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import kotlin.time.Instant
import androidx.compose.foundation.lazy.items
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.add
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

fun Instant.toDisplayDate(): String {
    val date = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val day = date.day.toString().padStart(2, '0')
    val month = date.month.number.toString().padStart(2, '0')
    return "$day/$month/${date.year}"
}

@Composable
fun SaveItem(
    house: House,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LightGreen, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Sauvegarde du ${house.saveDate?.toDisplayDate() ?: "--/--/----"}",
            color = WhiteBackground,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Text(
            text = "${house.rooms.size} pièce${if (house.rooms.size > 1) "s" else ""}",
            color = WhiteBackground.copy(alpha = 0.85f),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        Button(
            onClick = onViewClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = WhiteBackground
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.6f)
        ) {
            Text("Voir")
        }
    }
}

@Composable
fun SavesPopup(
    stores: Stores,
    onViewHouse: (House) -> Unit = {},
    onAddClick: () -> Unit = {}
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
            .padding(20.dp)
    ) {
        Text(
            text = "Sauvegardes",
            color = WhiteBackground,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(modifier = Modifier.weight(1f, fill = false)) {
            when {
                houseState.loading && houseState.savedHouses.isEmpty() -> {
                    CircularProgressIndicator(
                        color = WhiteBackground,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }

                houseState.savedHouses.isEmpty() -> {
                    Text(
                        text = "Aucune sauvegarde pour le moment",
                        color = WhiteBackground.copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                            //.padding(end = 16.dp) // laisse la place à la scrollbar
                    ) {
                        items(houseState.savedHouses, key = { it.id }) { house ->
                            SaveItem(
                                house = house,
                                onViewClick = { onViewHouse(house) }
                            )
                        }
                    }

                    // TODO: TO review, do we really need it?
                    /* CustomVerticalScrollbar(
                        listState = listState,
                        itemCount = houseState.savedHouses.size,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .padding(vertical = 4.dp)
                    ) */
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FloatingActionButton(
            onClick = onAddClick,
            containerColor = LightGreen,
            contentColor = WhiteBackground,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.Start)
                .size(48.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.add),
                contentDescription = "Ajouter une sauvegarde",
            )
        }
    }
}
