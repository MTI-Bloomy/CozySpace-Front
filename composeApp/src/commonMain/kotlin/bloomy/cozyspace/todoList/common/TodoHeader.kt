package bloomy.cozyspace.todoList.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.todoList.component.TodoCategory
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.Category
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TodoHeader(isTodoList: Boolean, tasks: Map<Category, List<Task>>, selectedCategory: Category?, onCategorySelected: (Category?) -> Unit) {
    val sdf = SimpleDateFormat("dd MMMM")
    val currentDate = sdf.format(Date())

    Column {
        Text(
            text = if (isTodoList) "Tasks" else "Completed tasks",
            color = DarkGreen,
            fontWeight = FontWeight.Black,
            fontSize = 42.sp,
            maxLines = 1
        )

        Text(
            text = currentDate,
            color = LightGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            maxLines = 1
        )

        Spacer(Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(180.dp)
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
                                category
                        )
                    }
                )
            }
        }
    }
}
