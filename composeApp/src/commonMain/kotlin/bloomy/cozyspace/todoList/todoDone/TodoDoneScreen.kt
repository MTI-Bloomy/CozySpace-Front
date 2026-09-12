package bloomy.cozyspace.todoList.todoDone

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoDoneStore
import bloomy.cozyspace.todoList.common.TodoHeader
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.utils.observeState
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
@Composable
fun TodoDoneScreen(fromTodoDone_toTodoList: () -> Unit = {}, stores: Stores) {
    val todoDoneState = stores.todoDone.observeState()

    LaunchedEffect(Unit) {
        stores.todoDone.accept(TodoDoneStore.Intent.GetTodoDone)
    }

    val tasks = mutableMapOf<Category, List<Todo>>()
    tasks[Category.Kitchen] = emptyList()
    tasks[Category.Bathroom] = emptyList()
    tasks[Category.Bedroom] = emptyList()
    tasks[Category.Garden] = emptyList()
    tasks[Category.Work] = emptyList()

    for (todoList in todoDoneState.todoDone) {
        when (todoList.type) {
            RoomType.KITCHEN -> tasks[Category.Kitchen] = tasks[Category.Kitchen]?.plus(todoList) as List<Todo>
            RoomType.BATHROOM -> tasks[Category.Bathroom] = tasks[Category.Bathroom]?.plus(todoList) as List<Todo>
            RoomType.BEDROOM -> tasks[Category.Bedroom] = tasks[Category.Bedroom]?.plus(todoList) as List<Todo>
            RoomType.GARDEN -> tasks[Category.Garden] = tasks[Category.Garden]?.plus(todoList) as List<Todo>
            RoomType.WORK -> tasks[Category.Work] = tasks[Category.Work]?.plus(todoList) as List<Todo>
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val isCompact = maxWidth < 600.dp
        val density = LocalDensity.current
        val keyboardOpen = WindowInsets.ime.getBottom(density) > 0
        var selectedCategory by remember {
            mutableStateOf<Category?>(null)
        }

        TodoDoneLayout(
            isCompact = isCompact,
            keyboardOpen = keyboardOpen,
            tasks = tasks,
            selectedCategory = selectedCategory,
            header = {
                TodoHeader(
                    isTodoList = false,
                    isCompact = isCompact,
                    tasks = tasks,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    onTodoScreenChange = fromTodoDone_toTodoList
                )
            }
        )
    }
}

