package bloomy.cozyspace.todoList.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.component.TodoItem
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.Category

@Composable
fun TodoLayout(isCompact: Boolean, keyboardOpen: Boolean, tasks: Map<Category, List<Task>>, selectedCategory: Category?, onTaskChecked: (String, Boolean) -> Unit, header: @Composable () -> Unit) {
    val filteredTasks = if (selectedCategory == null) {
        tasks.values.flatten()
    } else {
        tasks[selectedCategory].orEmpty()
    }

    if (isCompact) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .padding(horizontal = 20.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (keyboardOpen) 0.4f else 1f),
                contentAlignment = Alignment.Center
            ) {
                header()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    items(filteredTasks) { task ->
                        TodoItem(
                            task,
                            onTaskChecked = { checked -> onTaskChecked(task.id, checked) }
                        )
                    }
                }
            }
        }
    } else {
        Row (modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(20.dp)
            .imePadding(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(
                    if (keyboardOpen) 1f else 1.5f
                ),
                contentAlignment = Alignment.Center
            ) {
                header()
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 500.dp)
                    .fillMaxWidth()
            ) {
                LazyColumn {
                    items(filteredTasks) { task ->
                        TodoItem(
                            task,
                            onTaskChecked = { checked -> onTaskChecked(task.id, checked) }
                        )
                    }
                }
            }
        }
    }
}
