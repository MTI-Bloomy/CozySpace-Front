package bloomy.cozyspace.todoList.utils

import bloomy.cozyspace.theme.DarkBlueBubble
import bloomy.cozyspace.theme.DarkGreenBubble
import bloomy.cozyspace.theme.DarkPurpleBubble
import bloomy.cozyspace.theme.DarkRedBubble
import bloomy.cozyspace.theme.DarkYellowBubble
import bloomy.cozyspace.theme.LightBlueBubble
import bloomy.cozyspace.theme.LightGreenBubble
import bloomy.cozyspace.theme.LightPurpleBubble
import bloomy.cozyspace.theme.LightRedBubble
import bloomy.cozyspace.theme.LightYellowBubble
import bloomy.cozyspace.theme.TransparentBlueBubble
import bloomy.cozyspace.theme.TransparentGreenBubble
import bloomy.cozyspace.theme.TransparentPurpleBubble
import bloomy.cozyspace.theme.TransparentRedBubble
import bloomy.cozyspace.theme.TransparentYellowBubble
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.todoCategoryBathroom
import cozyspace.composeapp.generated.resources.todoCategoryBedroom
import cozyspace.composeapp.generated.resources.todoCategoryGarden
import cozyspace.composeapp.generated.resources.todoCategoryKitchen
import cozyspace.composeapp.generated.resources.todoCategoryWork
import cozyspace.composeapp.generated.resources.todoItem_More
import org.jetbrains.compose.resources.DrawableResource

enum class Category(
    val colors: CategoryColors,
    val icon : DrawableResource
) {

    Kitchen(
        CategoryColors(
            dark = DarkYellowBubble,
            light = LightYellowBubble,
            transparent = TransparentYellowBubble
        ),
        Res.drawable.todoCategoryKitchen
    ),

    Work(
        CategoryColors(
            dark = DarkPurpleBubble,
            light = LightPurpleBubble,
            transparent = TransparentPurpleBubble
        ),
        Res.drawable.todoCategoryWork
    ),

    Bedroom(
        CategoryColors(
            dark = DarkRedBubble,
            light = LightRedBubble,
            transparent = TransparentRedBubble
        ),
        Res.drawable.todoCategoryBedroom
    ),

    Garden(
        CategoryColors(
            dark = DarkGreenBubble,
            light = LightGreenBubble,
            transparent = TransparentGreenBubble
        ),
        Res.drawable.todoCategoryGarden
    ),

    Bathroom(
        CategoryColors(
            dark = DarkBlueBubble,
            light = LightBlueBubble,
            transparent = TransparentBlueBubble
        ),
        Res.drawable.todoCategoryBathroom
    )
}
