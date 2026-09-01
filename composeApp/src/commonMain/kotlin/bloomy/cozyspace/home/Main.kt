package bloomy.cozyspace.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.UserStore
import bloomy.cozyspace.utils.observeState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.cache.Storages
import bloomy.cozyspace.home.components.RoomView
import bloomy.cozyspace.home.components.SavesPopup
import bloomy.cozyspace.store.HouseStore
import bloomy.cozyspace.store.RewardStore
import bloomy.cozyspace.store.RoomStore
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.utils.CustomDialogBox
import bloomy.cozyspace.utils.LoadingScreen
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.arrow_forward
import cozyspace.composeapp.generated.resources.file_save_off
import cozyspace.composeapp.generated.resources.logout
import cozyspace.composeapp.generated.resources.save
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeMain(stores: Stores, storages: Storages) {
    val houseState = stores.house.observeState()
    val roomState = stores.room.observeState()
    val rewardState = stores.reward.observeState()

    var showSavesPopup by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        stores.house.accept(HouseStore.Intent.GetHouse)
        stores.reward.accept(RewardStore.Intent.GetRewards)
    }

    if (houseState.house != null) {
        LaunchedEffect(Unit) {
            stores.room.accept(RoomStore.Intent.GetRooms(houseState.house!!.id))
        }
    }

    var saveHouseViewMode by remember { mutableStateOf(false) }

    // Room actuellement affichée, trackée par id
    var currentRoomId by remember { mutableStateOf<String?>(null) }

    // Direction de la dernière navigation : 1 = suivant (slide vers la gauche), -1 = précédent (slide vers la droite)
    var navigationDirection by remember { mutableIntStateOf(1) }

    LaunchedEffect(roomState.rooms) {
        if (roomState.rooms.none { it.id == currentRoomId }) {
            currentRoomId = roomState.rooms.firstOrNull()?.id
        }
    }

    val currentRoomIndex = roomState.rooms.indexOfFirst { it.id == currentRoomId }
    val currentRoom = roomState.rooms.getOrNull(currentRoomIndex)

    fun goToRoom(offset: Int) {
        if (roomState.rooms.isEmpty() || currentRoomIndex == -1) return
        navigationDirection = offset
        val newIndex = (currentRoomIndex + offset).mod(roomState.rooms.size)
        currentRoomId = roomState.rooms[newIndex].id
    }

    fun goToPreviousRoom() = goToRoom(-1)
    fun goToNextRoom() = goToRoom(1)

    val density = LocalDensity.current
    val swipeThresholdPx = remember(density) { with(density) { 80.dp.toPx() } }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!saveHouseViewMode) {
                        Button(
                            onClick = {
                                showSavesPopup = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkGreen,
                            ),
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.save),
                                contentDescription = "Sauvegardes",
                                tint = WhiteBackground,
                            )
                        }
                    } else {
                        Text("Saves: Currently on view mode only")
                        Button(
                            onClick = {
                                saveHouseViewMode = false

                                currentRoomId = null

                                stores.room.accept(RoomStore.Intent.GetRooms(houseState.house!!.id))
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkGreen,
                            ),
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.file_save_off),
                                contentDescription = "Exit save view mode",
                                tint = WhiteBackground,
                            )
                        }
                    }
                    Button(
                        onClick = {
                            stores.user.accept(UserStore.Intent.Logout)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGreen,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.logout),
                            contentDescription = "Se déconnecter",
                            tint = WhiteBackground,
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(WhiteBackground).padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            if (currentRoom != null) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { goToPreviousRoom() }) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_forward),
                            contentDescription = "Room précédente",
                            modifier = Modifier
                                .size(36.dp)
                                .graphicsLayer { scaleX = -1f }, // flip pour pointer à gauche
                            tint = DarkGreen,
                        )
                    }

                    AnimatedContent(
                        targetState = currentRoomId,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .pointerInput(currentRoomId) {
                                var totalDrag = 0f
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        when {
                                            totalDrag > swipeThresholdPx -> goToPreviousRoom()
                                            totalDrag < -swipeThresholdPx -> goToNextRoom()
                                        }
                                        totalDrag = 0f
                                    },
                                ) { change, dragAmount ->
                                    change.consume()
                                    totalDrag += dragAmount
                                }
                            },
                        contentAlignment = Alignment.Center,
                        transitionSpec = {
                            val direction = if (navigationDirection >= 0) {
                                AnimatedContentTransitionScope.SlideDirection.Left
                            } else {
                                AnimatedContentTransitionScope.SlideDirection.Right
                            }
                            slideIntoContainer(direction, animationSpec = tween(300)) togetherWith
                                slideOutOfContainer(direction, animationSpec = tween(300))
                        },
                        label = "room_transition",
                    ) { targetRoomId ->
                        val room = roomState.rooms.firstOrNull { it.id == targetRoomId }
                        if (room != null) {
                            RoomView(
                                roomType = room.type,
                                rewards = rewardState.rewards.filter { reward -> room.furniture.contains(reward.id) },
                                storages = storages,
                            )
                        }
                    }

                    IconButton(onClick = { goToNextRoom() }) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_forward),
                            contentDescription = "Room suivante",
                            modifier = Modifier.size(36.dp),
                            tint = DarkGreen,
                        )
                    }
                }
            } else {
                LoadingScreen()
            }
        }
    }

    if (showSavesPopup) {
        CustomDialogBox(
            onDismiss = { showSavesPopup = false },
        ) {
            SavesPopup(
                stores = stores,
                onViewHouse = {
                    showSavesPopup = false
                    saveHouseViewMode = true

                    currentRoomId = null

                    stores.room.accept(RoomStore.Intent.GetRooms(it.id, true))
                },
                onAddClick = {
                    stores.house.accept(HouseStore.Intent.SaveHouse(houseState.house!!.id))
                },
            )
        }
    }
}
