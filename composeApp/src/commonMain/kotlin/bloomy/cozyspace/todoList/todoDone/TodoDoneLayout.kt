package bloomy.cozyspace.todoList.todoDone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.component.AddTodoButton
import bloomy.cozyspace.todoList.component.NewTodoItem
import bloomy.cozyspace.todoList.component.TodoDoneItem
import bloomy.cozyspace.todoList.component.TodoItem
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.Spacing
import kotlin.collections.orEmpty


@Composable
fun TodoDoneLayout(
    isCompact: Boolean,
    keyboardOpen: Boolean,
    tasks: MutableMap<Category, List<Todo>>,
    selectedCategory: Category?,
    header: @Composable () -> Unit,
) {
    val filteredTasks = if (selectedCategory == null) {
        tasks.values.flatten()
    } else {
        tasks[selectedCategory].orEmpty()
    }

    if (isCompact) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 20.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                header()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    items(filteredTasks) { task ->
                        TodoDoneItem(
                                task,
                            )
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(20.dp)
                .imePadding(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xl, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(
                    if (keyboardOpen) 1f else 1.5f,
                ),
                contentAlignment = Alignment.Center,
            ) {
                header()
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(),
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    items(filteredTasks) { task ->
                        TodoDoneItem(
                                task,
                            )
                    }
                }
            }
        }
    }
}
