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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.domain.RoomType
import bloomy.cozyspace.domain.Todo
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.utils.CategoryName
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoItem_More
import cozyspace.composeapp.generated.resources.todoItem_Tick
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoDoneItem(task: Todo, clicked: () -> Unit = {}) {
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
                    .background(MidDarkGreen),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.todoItem_Tick),
                    contentDescription = "More",
                    tint = WhiteBackground,
                    modifier = Modifier.size(28.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center text section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .alpha(0.6f),
            ) {
                // Task name
                Text(
                    text = task.name,
                    color = WhiteBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    textDecoration = TextDecoration.LineThrough,
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
//                    modifier = Modifier
//                        .alpha(0.6f),
//                    horizontalAlignment = Alignment.End
//                ) {
//                    // Ex: 2026-05-04T18:00:00
//                    // Only keeps the first 5 characters after T
//                    val date = task.startDate.substringBefore('T').take(10)
//
//                    Text(
//                        text = date,
//                        color = WhiteBackground,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold
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
//                        color = WhiteBackground,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(8.dp))
//            }

            // More button
            IconButton(
                onClick = { },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.todoItem_More),
                    contentDescription = "More",
                    tint = WhiteBackground,
                )
            }
        }
    }
}

