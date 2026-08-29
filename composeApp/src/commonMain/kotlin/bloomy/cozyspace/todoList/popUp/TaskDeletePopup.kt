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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.component.microComponent.LabelButton
import bloomy.cozyspace.todoList.utils.CustomDialogBox

@Composable
fun TaskDeletePopup(
    task: Todo,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss,
) {
    CustomDialogBox(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Are you sure you want to delete this task ?",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
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
                    text = "Delete",
                    contentColor = DarkGreen,
                    backgroundColor = WhiteBackground,
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm,
                )
            }
        }
    }
}
