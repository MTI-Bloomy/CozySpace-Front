package bloomy.cozyspace.todoList.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.todoList.component.TodoCategory
import bloomy.cozyspace.todoList.component.TodoCorner
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.Spacing
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TodoHeader(
    isTodoList: Boolean,
    isCompact: Boolean,
    tasks: Map<Category, List<Todo>>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    onTodoScreenChange: () -> Unit,
) {
    val sdf = SimpleDateFormat("dd MMMM")
    val currentDate = sdf.format(Date())

    // To keep the corner in the top left in landscape mode and desktop version
    // Also
    val mirrored = isCompact && isTodoList

    val titleFontSize = 42.sp
    val titleText = if (isTodoList) "Tasks" else if (isCompact) "Completed\ntasks" else "Completed tasks"

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (mirrored) Alignment.Start else Alignment.End,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                text = titleText,
                textAlign = if (mirrored) TextAlign.Start else TextAlign.End,
                color = DarkGreen,
                fontWeight = FontWeight.Black,
                fontSize = titleFontSize,
                lineHeight = titleFontSize * 1.15f,
            )

            Text(
                text = currentDate,
                color = LightGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                maxLines = 1,
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(180.dp),
            ) {
                items(tasks.entries.toList()) { entry ->
                    val category = entry.key
                    val taskList = entry.value

                    TodoCategory(
                        category = category,
                        nbTasks = taskList.size,
                        isSelected = selectedCategory == category,
                        onClick = {
                            onCategorySelected(
                                if (selectedCategory == category)
                                    null
                                else
                                    category,
                            )
                        },
                    )
                }
            }
        }

        TodoCorner(
            isTodoList = isTodoList,
            mirrored = mirrored,
            onClick = onTodoScreenChange,
            modifier = Modifier.align(if (!isCompact || !isTodoList) Alignment.TopStart else Alignment.TopEnd),
        )
    }
}
