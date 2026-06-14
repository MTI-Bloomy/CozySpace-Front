package bloomy.cozyspace.todoList.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.todoList.domain.Task
import bloomy.cozyspace.theme.LightGreen
import bloomy.cozyspace.theme.MidDarkGreen
import bloomy.cozyspace.theme.WhiteBackground
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoItem_More
import cozyspace.composeapp.generated.resources.todoItem_Tick
import org.jetbrains.compose.resources.painterResource

@Composable
fun TodoItem(task: Task, clicked: () -> Unit = {}, onTaskChecked: (Boolean) -> Unit = {}) {
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
                    .clickable {
                        onTaskChecked(!task.isDone)
                    }
            ) {
                if (task.isDone) {
                    Icon(
                        painter = painterResource(Res.drawable.todoItem_Tick),
                        contentDescription = "More",
                        tint = WhiteBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center text section
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Task name
                Text(
                    text = task.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Task category
                TodoItemCategory(task.type)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Date section (optional)
            if (task.startDate.isNotBlank()) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = task.startDate,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Text(
//                        text = "16:00",
//                        color = Color.White.copy(alpha = 0.85f),
//                        fontSize = 11.sp
//                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

            // More button
            IconButton(
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.todoItem_More),
                    contentDescription = "More",
                    tint = Color.White
                )
            }
        }
    }
}
