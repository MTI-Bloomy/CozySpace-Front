package bloomy.cozyspace.todoList.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.WhiteBackground
import bloomy.cozyspace.todoList.utils.Category
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.arrow_down
import cozyspace.composeapp.generated.resources.arrow_up
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = selectedCategory.colors
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = WhiteBackground,
            border = BorderStroke(1.dp, colors.dark),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCategory.name,
                    color = colors.dark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = painterResource(if (expanded) Res.drawable.arrow_up else Res.drawable.arrow_down),
                    contentDescription = null,
                    tint = colors.dark,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Category.entries.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.name,
                            color = category.colors.dark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    modifier = Modifier.widthIn(min = 150.dp)
                )
            }
        }
    }
}
