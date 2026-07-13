package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.MidLightGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.domain.Task
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.calendar
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeDetails(
    task: Task,
    onDateTimeSelected: (String?) -> Unit = {}
) {
    var isDateEnabled by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    fun notifyChange() {
        val millis = selectedDateMillis
        if (millis == null) {
            onDateTimeSelected(null)
            return
        }
        val date = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val localDateTime = LocalDateTime(date, LocalTime(selectedHour, selectedMinute))
        onDateTimeSelected(localDateTime.toString())
    }

    val dateLabel = selectedDateMillis?.let { millis ->
        val date = Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        "${date.day.toString().padStart(2, '0')}/" +
            "${date.month.number.toString().padStart(2, '0')}/" +
            "${date.year}"
    } ?: "No date"

    val timeLabel = "${selectedHour.toString().padStart(2, '0')}:" +
        selectedMinute.toString().padStart(2, '0')

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkGreen,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(WhiteBackground)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.calendar),
                    contentDescription = "Calendar",
                    tint = DarkGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Date",
                    color = WhiteBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = WhiteBackground.copy(alpha = if (isDateEnabled) 0.18f else 0.08f),
                        modifier = Modifier
                            .clickable(enabled = isDateEnabled) { showDatePicker = true }
                    ) {
                        Text(
                            text = dateLabel,
                            color = WhiteBackground.copy(alpha = if (isDateEnabled) 1f else 0.5f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = WhiteBackground.copy(alpha = if (isDateEnabled) 0.18f else 0.08f),
                        modifier = Modifier
                            .clickable(enabled = isDateEnabled) { showTimePicker = true }
                    ) {
                        Text(
                            text = timeLabel,
                            color = WhiteBackground.copy(alpha = if (isDateEnabled) 1f else 0.5f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = isDateEnabled,
                onCheckedChange = { checked ->
                    isDateEnabled = checked
                    if (!checked) {
                        selectedDateMillis = null
                        selectedHour = 0
                        selectedMinute = 0
                        onDateTimeSelected(null)
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = WhiteBackground,
                    checkedTrackColor = MidLightGreen,
                    checkedBorderColor = MidLightGreen,
                    checkedIconColor = DarkGreen,

                    uncheckedThumbColor = WhiteBackground,
                    uncheckedTrackColor = WhiteBackground.copy(alpha = 0.3f),
                    uncheckedBorderColor = WhiteBackground.copy(alpha = 0.3f),
                    uncheckedIconColor = DarkGreen
                )
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateMillis = datePickerState.selectedDateMillis
                        showDatePicker = false
                        notifyChange()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = true
        )

        if (selectedDateMillis == null) {
            selectedDateMillis = Clock.System.now().toEpochMilliseconds()
        }

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        showTimePicker = false
                        notifyChange()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
