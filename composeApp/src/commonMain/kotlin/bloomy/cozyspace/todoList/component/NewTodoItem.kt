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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.popUp.TaskDetailPopup
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.CategoryName
import bloomy.cozyspace.todoList.utils.Frequency
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoItem_More
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewTodoItem(
    onCreate: (name: String, category: Category, frequency: Frequency, startDate: String?) -> Unit,
    clicked: () -> Unit = {}
) {
    var taskName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.entries.first()) }
    var selectedFrequency by remember { mutableStateOf(Frequency.Never) }
    var startDate by remember { mutableStateOf<String?>(null) }
    var hasBeenCreated by remember { mutableStateOf(false) }
    var showDetailPopup by remember { mutableStateOf(false) }

    fun tryCreate() {
        if (!hasBeenCreated && taskName.isNotBlank()) {
            hasBeenCreated = true
            onCreate(taskName, selectedCategory, selectedFrequency, startDate)
        }
    }

    // Draft task built from current local state, just to feed the popup's preview
    val draftTask = Task(
        id = "",
        name = taskName,
        frequency = selectedFrequency.days,
        type = CategoryName.valueOf(selectedCategory.name),
        startDate = startDate.orEmpty(),
        isDone = false
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGreen,
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
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MidDarkGreen)
            ) {}

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = WhiteBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(WhiteBackground),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { tryCreate() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) {
                                tryCreate()
                            }
                        },
                    decorationBox = { innerTextField ->
                        Box {
                            if (taskName.isEmpty()) {
                                Text(
                                    "Write a new task",
                                    color = WhiteBackground.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    maxLines = 1
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(2.dp))

                CategoryDropdown(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            IconButton(
                onClick = { showDetailPopup = true }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.todoItem_More),
                    contentDescription = "More",
                    tint = WhiteBackground
                )
            }
        }
    }

    if (showDetailPopup) {
        TaskDetailPopup(
            task = draftTask,
            onDismiss = { showDetailPopup = false },
            onDateTimeSelected = { newDate -> startDate = newDate },
            onFrequencySelected = { newFrequency -> selectedFrequency = newFrequency },
            onCategorySelected = { newCategory -> selectedCategory = newCategory }
        )
    }
}
