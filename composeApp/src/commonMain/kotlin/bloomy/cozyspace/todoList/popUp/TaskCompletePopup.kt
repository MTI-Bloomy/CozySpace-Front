package bloomy.cozyspace.todoList.popUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import bloomy.cozyspace.utils.CustomDialogBox


@Composable
fun TaskCompletePopup(
    task: Todo,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss
) {
    CustomDialogBox(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "You completed a task !",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Choose a decoration to place",
                color = DarkGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LabelButton(
                    text = "Go !",
                    contentColor = DarkGreen,
                    backgroundColor = WhiteBackground,
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm
                )

                Spacer(modifier = Modifier.width(12.dp))

                LabelButton(
                    text = "Check later",
                    contentColor = WhiteBackground,
                    backgroundColor = DarkGreen,
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )
            }
        }
    }
}
