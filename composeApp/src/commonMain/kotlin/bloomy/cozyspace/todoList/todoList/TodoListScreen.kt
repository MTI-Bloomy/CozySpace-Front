package bloomy.cozyspace.todoList

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import bloomy.cozyspace.todoList.common.TodoHeader
import bloomy.cozyspace.todoList.common.TodoLayout
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.CategoryName
import bloomy.cozyspace.todoList.utils.Frequency
import kotlin.collections.Map
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun TodoListScreen(fromTodoList_toTodoDone: () -> Unit = {}) {
    // TODO => remove
    var tasks: Map<Category, List<Task>> by remember {
        mutableStateOf(
            mapOf(
                Category.Kitchen to listOf(
                    Task(
                        id = "1",
                        name = "Do the dishes",
                        frequency = 1,
                        type = CategoryName.Kitchen,
                        startDate = "2026-05-04T18:00:00",
                        isDone = false
                    ),
                    Task(
                        id = "2",
                        name = "Clean fridge",
                        frequency = 7,
                        type = CategoryName.Kitchen,
                        startDate = "2026-05-04T18:00:00",
                        isDone = false
                    )
                ),

                Category.Work to listOf(
                    Task(
                        id = "3",
                        name = "Finish report",
                        frequency = 1,
                        type = CategoryName.Work,
                        startDate = "2026-05-04T18:00:00",
                        isDone = false
                    )
                ),

                Category.Bedroom to listOf(
                    Task(
                        id = "4",
                        name = "Change sheets",
                        frequency = 14,
                        type = CategoryName.Bedroom,
                        startDate = "2026-05-04T18:00:00",
                        isDone = false
                    ),
                    Task(
                        id = "5",
                        name = "Vacuum room",
                        frequency = 7,
                        type = CategoryName.Bedroom,
                        startDate = "2026-05-04T18:00:00",
                        isDone = false
                    )
                ),

                Category.Garden to emptyList(),

                Category.Bathroom to listOf(
                    Task(
                        id = "6",
                        name = "Clean mirror",
                        frequency = 7,
                        type = CategoryName.Bathroom,
                        startDate = "07/05/2026 08:00",
                        isDone = false
                    ),
                    Task(
                        id = "7",
                        name = "Clean the bathtub",
                        frequency = 7,
                        type = CategoryName.Bathroom,
                        startDate = "07/05/2026 08:00",
                        isDone = false
                    ),
                    Task(
                        id = "8",
                        name = "Clean the sink",
                        frequency = 7,
                        type = CategoryName.Bathroom,
                        startDate = "07/05/2026 08:00",
                        isDone = false
                    )
                )
            )
        )
    }

    // Copies isDone for the modified task
    fun onTaskChecked(taskId: String, isDone: Boolean) {
        tasks = tasks.mapValues { (_, list) ->
            list.map { t -> if (t.id == taskId) t.copy(isDone = isDone) else t }
        }
    }

    // Creates a new task and adds it to its category's list
    fun onTaskCreated(name: String, category: Category, frequency: Frequency, startDate: String?) {
        val newTask = Task(
            id = Uuid.random().toString(),
            name = name,
            frequency = frequency.days,
            type = CategoryName.valueOf(category.name),
            startDate = startDate.orEmpty(),
            isDone = false
        )

        tasks = tasks.toMutableMap().apply {
            val currentList = this[category].orEmpty()
            this[category] = currentList + newTask
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

        TodoLayout(
            isCompact = isCompact,
            isTodoList = true,
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
