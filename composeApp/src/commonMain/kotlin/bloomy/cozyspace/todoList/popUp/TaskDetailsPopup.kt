package bloomy.cozyspace.todoList.popUp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.component.DateTimeDetails
import bloomy.cozyspace.todoList.component.FrequencyDetails
import bloomy.cozyspace.todoList.component.TodoName
import bloomy.cozyspace.todoList.component.microComponent.LabelButton
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.Frequency

@Composable
fun TaskDetailPopup(
    task: Task,
    onDismiss: () -> Unit,
    onCategorySelected: (Category) -> Unit,
    onDateTimeSelected: (String?) -> Unit,
    onFrequencySelected: (Frequency) -> Unit,
    onConfirm: () -> Unit = onDismiss
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* doesn't close the popup */ },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WhiteBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Details",
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    TodoName(task = task, onCategorySelected = onCategorySelected)

                    DateTimeDetails(
                        task = task,
                        onDateTimeSelected = onDateTimeSelected
                    )

                    FrequencyDetails(
                        task = task,
                        onFrequencySelected = onFrequencySelected
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LabelButton(
                            text = "Cancel",
                            contentColor = WhiteBackground,
                            backgroundColor = DarkGreen,
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        LabelButton(
                            text = "Confirm",
                            contentColor = DarkGreen,
                            backgroundColor = WhiteBackground,
                            modifier = Modifier.weight(1f),
                            onClick = onConfirm
                        )
                    }
                }
            }
        }
    }
}
