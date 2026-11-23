package bloomy.cozyspace.timers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen

@Composable
fun DurationPicker(
    title: String,
    durationMinutes: Int,
    onDurationChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxHours: Int = 12,
    showContainer: Boolean = true,
    compact: Boolean = false,
) {
    val hours = durationMinutes / 60
    val minutes = durationMinutes % 60
    var hoursText by remember { mutableStateOf(hours.toString().padStart(2, '0')) }
    var minutesText by remember { mutableStateOf(minutes.toString().padStart(2, '0')) }

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
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 10.dp),
    ) {
        Text(title, color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = if (compact) 14.sp else 17.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DurationInput(
                value = hoursText,
                label = "hours",
                onValueChange = { input ->
                    val digits = input.filter(Char::isDigit).take(2)
                    val parsed = digits.toIntOrNull()?.coerceIn(0, maxHours) ?: 0
                    hoursText = if (digits.isEmpty()) "" else parsed.toString()
                    onDurationChange(parsed * 60 + (minutesText.toIntOrNull() ?: 0).coerceIn(0, 59))
                },
                modifier = Modifier.weight(1f),
                compact = compact,
            )
            Text(":", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            DurationInput(
                value = minutesText,
                label = "minutes",
                onValueChange = { input ->
                    val parsed = input.filter(Char::isDigit).take(2).toIntOrNull()?.coerceIn(0, 59)
                    minutesText = if (input.isEmpty()) "" else (parsed ?: 0).toString()
                    onDurationChange((hoursText.toIntOrNull() ?: 0).coerceIn(0, maxHours) * 60 + (parsed ?: 0))
                },
                modifier = Modifier.weight(1f),
                compact = compact,
            )
        }

    }
}

@Composable
private fun DurationInput(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Column(
        modifier = modifier.padding(horizontal = if (compact) 4.dp else 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(if (compact) 0.dp else 4.dp),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = DarkGreen,
                fontSize = if (compact) 22.sp else 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(DarkGreen),
            modifier = Modifier
                .size(width = if (compact) 64.dp else 76.dp, height = if (compact) 42.dp else 50.dp)
                .background(MidDarkGreen.copy(alpha = 0.22f), RoundedCornerShape(10.dp))
                .border(1.dp, MidDarkGreen, RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = if (compact) 6.dp else 10.dp),
        )
        Text(label, color = DarkGreen.copy(alpha = 0.75f), fontSize = if (compact) 10.sp else 12.sp)
    }
}
