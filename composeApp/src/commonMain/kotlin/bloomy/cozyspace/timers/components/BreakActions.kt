package bloomy.cozyspace.timers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen
import bloomy.cozyspace.theme.WhiteBackground

@Composable
fun BreakActions(
    selectedActions: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
    showContainer: Boolean = true,
    showTitle: Boolean = true,
    compact: Boolean = false,
) {
    val actions = listOf(
        "Stretch or take a short walk",
        "Breathe deeply",
        "Tidy your desk",
        "Drink a glass of water",
    )

    val containerModifier = if (showContainer) {
        modifier
            .fillMaxWidth()
            .background(LightGreen.copy(alpha = 0.22f), RoundedCornerShape(18.dp))
            .border(1.dp, DarkGreen.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(if (compact) 0.dp else 16.dp)
    } else {
        modifier.fillMaxWidth()
    }

    Column(
        modifier = containerModifier,
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp),
    ) {
        if (showTitle) {
            Text("Have a break", color = DarkGreen, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 17.sp)
        }
        Text(
            "Choose a few relaxing things to do.",
            color = DarkGreen.copy(alpha = 0.72f),
            fontSize = if (compact) 11.sp else 13.sp,
        )
        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                actions.chunked(2).forEach { rowActions ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        rowActions.forEach { action ->
                            BreakActionRow(
                                action = action,
                                checked = action in selectedActions,
                                compact = true,
                                modifier = Modifier.weight(1f),
                                onCheckedChange = { checked ->
                                    onSelectionChange(
                                        if (checked) selectedActions + action else selectedActions - action,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        } else {
            actions.forEach { action ->
                BreakActionRow(
                    action = action,
                    checked = action in selectedActions,
                    onCheckedChange = { checked ->
                        onSelectionChange(
                            if (checked) selectedActions + action else selectedActions - action,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun BreakActionRow(
    action: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MidDarkGreen.copy(alpha = 0.82f), RoundedCornerShape(12.dp))
            .padding(horizontal = if (compact) 2.dp else 8.dp, vertical = if (compact) 0.dp else 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = WhiteBackground,
                uncheckedColor = WhiteBackground,
                checkmarkColor = DarkGreen,
            ),
        )
        Text(action, color = WhiteBackground, fontSize = if (compact) 11.sp else 13.sp)
    }
}
