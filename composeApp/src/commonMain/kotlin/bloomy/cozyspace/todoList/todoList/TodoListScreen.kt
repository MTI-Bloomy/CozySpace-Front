package bloomy.cozyspace.todoList

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
import bloomy.cozyspace.data.dto.TodoRequestDto
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.store.Stores
import bloomy.cozyspace.store.TodoListStore
import bloomy.cozyspace.todoList.common.TodoHeader
import bloomy.cozyspace.todoList.common.TodoListLayout
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.Frequency
import bloomy.cozyspace.utils.observeState
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun TodoListScreen(fromTodoList_toTodoDone: () -> Unit = {}, stores: Stores) {
    val todoListState = stores.todoList.observeState()

    LaunchedEffect(Unit) {
        stores.todoList.accept(TodoListStore.Intent.GetTodoList)
    }

    val tasks = mutableMapOf<Category, List<Todo>>()
    tasks[Category.Kitchen] = emptyList()
    tasks[Category.Bathroom] = emptyList()
    tasks[Category.Bedroom] = emptyList()
    tasks[Category.Garden] = emptyList()
    tasks[Category.Work] = emptyList()

    for (todoList in todoListState.todoList) {
        when (todoList.type) {
            RoomType.KITCHEN -> tasks[Category.Kitchen] = tasks[Category.Kitchen]?.plus(todoList) as List<Todo>
            RoomType.BATHROOM -> tasks[Category.Bathroom] = tasks[Category.Bathroom]?.plus(todoList) as List<Todo>
            RoomType.BEDROOM -> tasks[Category.Bedroom] = tasks[Category.Bedroom]?.plus(todoList) as List<Todo>
            RoomType.GARDEN -> tasks[Category.Garden] = tasks[Category.Garden]?.plus(todoList) as List<Todo>
            RoomType.WORK -> tasks[Category.Work] = tasks[Category.Work]?.plus(todoList) as List<Todo>
        }
    }

    fun onTaskChecked(taskId: String) {
        stores.todoList.accept(TodoListStore.Intent.CompleteTodo(taskId))
    }

    // Creates a new task and adds it to its category's list
    fun onTaskCreated(name: String, category: Category, frequency: Frequency, nextDueDate: Instant) {
        val newTask = TodoRequestDto(
            name = name,
            frequency = frequency.days,
            type = category.name,
            nextDueDate = nextDueDate,
        )

        stores.todoList.accept(TodoListStore.Intent.CreateTodo(newTask))
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

        TodoListLayout(
            isCompact = isCompact,
            keyboardOpen = keyboardOpen,
            tasks = tasks,
            selectedCategory = selectedCategory,
            onTaskChecked = ::onTaskChecked,
            onTaskCreated = ::onTaskCreated,
            header = {
                TodoHeader(
                    isTodoList = true,
                    isCompact = isCompact,
                    tasks = tasks,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    onTodoScreenChange = fromTodoList_toTodoDone
                )
            }
        )
    }
}
