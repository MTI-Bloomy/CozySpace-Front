package bloomy.cozyspace.todoList.popUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.component.FrequencyDetails
import bloomy.cozyspace.todoList.component.TodoName
import bloomy.cozyspace.todoList.component.microComponent.LabelButton
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.Frequency
import bloomy.cozyspace.utils.CustomDialogBox

@Composable
fun TaskDetailsPopup(
    task: Todo,
    onDismiss: () -> Unit,
    onNameChanged: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onFrequencySelected: (Frequency) -> Unit,
    onDelete: (String) -> Unit,
    onConfirm: () -> Unit = onDismiss,
) {
    var currentName by remember(task.id) { mutableStateOf(task.name) }
    val isNameValid = currentName.isNotBlank()

    CustomDialogBox(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Details",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            TodoName(
                task = task,
                onNameChanged = onNameChanged,
                onCategorySelected = onCategorySelected,
                onDelete = onDelete,
            )

            FrequencyDetails(
                task = task,
                onFrequencySelected = onFrequencySelected,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LabelButton(
                    text = "Cancel",
                    contentColor = WhiteBackground,
                    backgroundColor = DarkGreen,
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss,
                )

                Spacer(modifier = Modifier.width(12.dp))

                LabelButton(
                    text = "Confirm",
                    contentColor = DarkGreen,
                    backgroundColor = WhiteBackground,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (isNameValid) 1f else 0.5f),
                    onClick = { if (isNameValid) onConfirm() },
                )
            }
        }
    }
}
