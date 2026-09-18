package bloomy.cozyspace.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.components.LabelButton
import bloomy.cozyspace.home.components.utils.Placement
import bloomy.cozyspace.todoList.utils.Frequency
import bloomy.cozyspace.utils.CustomDialogBox
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.arrow_down
import cozyspace.composeapp.generated.resources.arrow_up
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCompletePopup(
    selectedPlacement: Placement,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss,
    onPlacementSelected: (Placement) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var currentPlacement by remember(selectedPlacement) { mutableStateOf(selectedPlacement) }

    CustomDialogBox(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "You completed a task !",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Choose a decoration to place",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Placement.entries.forEach { placement ->
                    val isSelected = placement == currentPlacement

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) DarkGreen else WhiteBackground.copy(alpha = 0.18f),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                currentPlacement = placement
                                onPlacementSelected(placement)
                            }
                    ) {
                        Text(
                            text = placement.name,
                            color = if (isSelected) WhiteBackground else DarkGreen,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabelButton(
                    text = "Claim later",
                    contentColor = DarkGreen,
                    backgroundColor = WhiteBackground,
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.width(12.dp))

                LabelButton(
                    text = "Add to the room !",
                    contentColor = WhiteBackground,
                    backgroundColor = DarkGreen,
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm
                )
            }
        }
    }
}
