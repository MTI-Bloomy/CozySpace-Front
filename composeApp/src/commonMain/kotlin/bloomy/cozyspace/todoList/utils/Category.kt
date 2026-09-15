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
import cozyspace.composeapp.generated.resources.shower
import cozyspace.composeapp.generated.resources.bed
import cozyspace.composeapp.generated.resources.nature
import cozyspace.composeapp.generated.resources.oven_gen
import cozyspace.composeapp.generated.resources.backpack
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
        Res.drawable.oven_gen
    ),

    Work(
        CategoryColors(
            dark = DarkPurpleBubble,
            light = LightPurpleBubble,
            transparent = TransparentPurpleBubble
        ),
        Res.drawable.backpack
    ),

    Bedroom(
        CategoryColors(
            dark = DarkRedBubble,
            light = LightRedBubble,
            transparent = TransparentRedBubble
        ),
        Res.drawable.bed
    ),

    Garden(
        CategoryColors(
            dark = DarkGreenBubble,
            light = LightGreenBubble,
            transparent = TransparentGreenBubble
        ),
        Res.drawable.nature
    ),

    Bathroom(
        CategoryColors(
            dark = DarkBlueBubble,
            light = LightBlueBubble,
            transparent = TransparentBlueBubble
        ),
        Res.drawable.shower
    )
}
