package bloomy.cozyspace.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.components.LabelButton
import bloomy.cozyspace.domain.Reward
import bloomy.cozyspace.domain.Room
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.utils.CustomDialogBox
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.add
import cozyspace.composeapp.generated.resources.close
import org.jetbrains.compose.resources.painterResource

private enum class InventoryTab {
    INVENTORY,
    MY_ROOM,
}

@Composable
fun InventoryPopup(
    room: Room,
    rooms: List<Room>,
    rewards: List<Reward>,
    inventory: List<Reward>,
    storages: Storages,
    onDismiss: () -> Unit,
    onPlaceItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(InventoryTab.INVENTORY) }
    var rewardToRemove by remember { mutableStateOf<Reward?>(null) }

    val placedRewardIds = rooms.flatMap { it.furniture }.toSet()
    val allRewards = (rewards + inventory).distinctBy { it.id }
    val roomRewards = allRewards
        .filter { it.id in room.furniture }
        .sortedBy { it.layer }
    val availableInventory = allRewards
        .filter { it.roomType == room.type && it.id !in placedRewardIds }
        .sortedBy { it.layer }

    CustomDialogBox(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGreen)
                .padding(20.dp),
        ) {
            Text(
                text = "Furniture",
                color = WhiteBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InventoryTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Surface(
                        onClick = { selectedTab = tab },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) LightGreen else WhiteBackground.copy(alpha = 0.15f),
                    ) {
                        Text(
                            text = if (tab == InventoryTab.INVENTORY) "Inventory" else "My Room",
                            color = WhiteBackground,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 10.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val displayedRewards =
                if (selectedTab == InventoryTab.INVENTORY) availableInventory else roomRewards
            if (displayedRewards.isEmpty()) {
                Text(
                    text = if (selectedTab == InventoryTab.INVENTORY) {
                        "No furniture available for this room"
                    } else {
                        "No furniture in this room yet"
                    },
                    color = WhiteBackground.copy(alpha = 0.75f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(displayedRewards, key = { it.id }) { reward ->
                        FurnitureItem(
                            roomType = room.type,
                            reward = reward,
                            storages = storages,
                            isPlaced = selectedTab == InventoryTab.MY_ROOM,
                            onClick = {
                                if (selectedTab == InventoryTab.INVENTORY) {
                                    onPlaceItem(reward.id)
                                } else {
                                    rewardToRemove = reward
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    rewardToRemove?.let { reward ->
        CustomDialogBox(onDismiss = { rewardToRemove = null }) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = "Remove this item from your room?",
                    color = DarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    LabelButton(
                        text = "Cancel",
                        contentColor = WhiteBackground,
                        backgroundColor = DarkGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { rewardToRemove = null },
                    )
                    LabelButton(
                        text = "Remove",
                        contentColor = DarkGreen,
                        backgroundColor = WhiteBackground,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onRemoveItem(reward.id)
                            rewardToRemove = null
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun FurnitureItem(
    roomType: RoomType,
    reward: Reward,
    storages: Storages,
    isPlaced: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = WhiteBackground,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoomView(
                roomType = roomType,
                rewards = listOf(reward),
                storages = storages,
                modifier = Modifier
                    .size(width = 112.dp, height = 80.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = reward.furnitureId,
                modifier = Modifier.weight(1f),
                color = DarkGreen,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(if (isPlaced) Res.drawable.close else Res.drawable.add),
                    contentDescription = if (isPlaced) "Remove furniture" else "Place furniture",
                    tint = DarkGreen,
                )
            }
        }
    }
}
