package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.Frequency
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.arrow_down
import cozyspace.composeapp.generated.resources.arrow_up
import cozyspace.composeapp.generated.resources.frequency
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyDetails(
    task: Task,
    onFrequencySelected: (Frequency) -> Unit = {}
) {
    var selectedFrequency by remember { mutableStateOf(Frequency.fromDays(task.frequency)) }
    var expanded by remember { mutableStateOf(false) }

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
                    painter = painterResource(Res.drawable.frequency),
                    contentDescription = "Frequency",
                    tint = DarkGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Frequency",
                    color = WhiteBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = WhiteBackground.copy(alpha = 0.18f),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedFrequency.name,
                                color = WhiteBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Icon(
                                painter = painterResource(if (expanded) Res.drawable.arrow_up else Res.drawable.arrow_down),
                                contentDescription = null,
                                tint = WhiteBackground,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Frequency.entries.forEach { frequency ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = frequency.name,
                                        color = if (frequency == selectedFrequency) DarkGreen else Color.Black,
                                        fontWeight = if (frequency == selectedFrequency) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                },
                                onClick = {
                                    selectedFrequency = frequency
                                    onFrequencySelected(frequency)
                                    expanded = false
                                },
                                modifier = Modifier.widthIn(min = 150.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
