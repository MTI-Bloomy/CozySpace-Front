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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.popUp.TaskDetailPopup
import bloomy.cozyspace.todoList.utils.Category
import bloomy.cozyspace.todoList.utils.CategoryName
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoItem_More
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoItem(
    task: Todo,
    clicked: () -> Unit = {},
    onTaskChecked: () -> Unit,
    onCategoryChanged: (String, Category) -> Unit = { _, _ -> },
) {
    var showDetailPopup by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clicked() }
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGreen,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MidDarkGreen)
                    .clickable {
                        onTaskChecked()
                    },
            ) { }

            Spacer(modifier = Modifier.width(12.dp))

            // Center text section
            Column(
                modifier = Modifier.weight(1f),
            ) {
                // Task name
                Text(
                    text = task.name,
                    color = WhiteBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Task category
                TodoItemCategory(
                    when (task.type) {
                        RoomType.KITCHEN -> CategoryName.Kitchen
                        RoomType.BATHROOM -> CategoryName.Bathroom
                        RoomType.BEDROOM -> CategoryName.Bedroom
                        RoomType.GARDEN -> CategoryName.Garden
                        RoomType.WORK -> CategoryName.Work
                    }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Date section (optional)
//            if (task.startDate.isNotBlank()) {
//                Column(
//                    horizontalAlignment = Alignment.End,
//                ) {
//                    // Ex: 2026-05-04T18:00:00
//                    // Only keeps the first 5 characters after T
//                    val date = task.startDate.substringBefore('T').take(10)
//
//                    Text(
//                        text = date,
//                        color = WhiteBackground,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    // Ex: 2026-05-04T18:00:00
//                    // Only keeps the first 5 characters after T
//                    val time = task.startDate.substringAfter('T').take(5)
//
//                    Text(
//                        text = time,
//                        color = WhiteBackground.copy(alpha = 0.85f),
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(8.dp))
//            }

            // More button
            IconButton(
                onClick = { showDetailPopup = true },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.todoItem_More),
                    contentDescription = "More",
                    tint = WhiteBackground,
                )
            }
        }
    }

    if (showDetailPopup) {
        TaskDetailPopup(
            task = task,
            onDismiss = { showDetailPopup = false },
            onDateTimeSelected = { /* à connecter */ },
            onFrequencySelected = { /* à connecter */ },
            onCategorySelected = { newCategory -> onCategoryChanged(task.id, newCategory) },
        )
    }
}
