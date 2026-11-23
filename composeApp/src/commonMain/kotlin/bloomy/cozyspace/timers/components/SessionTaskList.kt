package bloomy.cozyspace.timers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen
import bloomy.cozyspace.theme.WhiteBackground

@Composable
fun SessionTaskList(
    tasks: List<String>,
    onTasksChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    showContainer: Boolean = true,
    compact: Boolean = false,
    showDescription: Boolean = true,
) {
    var newTask by remember { mutableStateOf("") }

    fun addTask() {
        val task = newTask.trim()
        if (task.isNotEmpty()) {
            onTasksChange(tasks + task)
            newTask = ""
        }
    }

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
        verticalArrangement = Arrangement.spacedBy(if (compact) 5.dp else 10.dp),
    ) {
        Text("Tasks for this session", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = if (compact) 14.sp else 17.sp)
        if (showDescription) {
            Text(
                "These tasks are only saved for this timer session.",
                color = DarkGreen.copy(alpha = 0.72f),
                fontSize = 13.sp,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(WhiteBackground, RoundedCornerShape(12.dp))
                .border(1.dp, MidDarkGreen, RoundedCornerShape(12.dp))
                .padding(start = 10.dp, end = 2.dp, top = if (compact) 2.dp else 7.dp, bottom = if (compact) 2.dp else 7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = newTask,
                onValueChange = { newTask = it },
                singleLine = true,
                textStyle = TextStyle(color = DarkGreen, fontSize = 14.sp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { addTask() }),
                cursorBrush = SolidColor(DarkGreen),
                decorationBox = { innerTextField ->
                    if (newTask.isEmpty()) {
                        Text("Add a task...", color = DarkGreen.copy(alpha = 0.55f), fontSize = 14.sp)
                    }
                    innerTextField()
                },
                modifier = Modifier.weight(1f).padding(vertical = if (compact) 4.dp else 8.dp),
            )
            androidx.compose.material3.TextButton(onClick = ::addTask) {
                Text("Add", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = if (compact) 13.sp else 14.sp)
            }
        }

        if (tasks.isNotEmpty()) {
            Column(
                modifier = if (compact) Modifier.heightIn(max = 80.dp).verticalScroll(rememberScrollState()) else Modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                tasks.forEachIndexed { index, task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MidDarkGreen, RoundedCornerShape(10.dp))
                            .padding(start = 10.dp, end = 2.dp, top = 2.dp, bottom = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = task,
                            color = WhiteBackground,
                            fontSize = if (compact) 12.sp else 14.sp,
                            modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                        )
                        androidx.compose.material3.TextButton(
                            onClick = { onTasksChange(tasks.filterIndexed { taskIndex, _ -> taskIndex != index }) },
                        ) {
                            Text("×", color = WhiteBackground, fontSize = 22.sp)
                        }
                    }
                }
            }
        }
    }
}
