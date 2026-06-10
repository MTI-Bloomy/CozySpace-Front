package bloomy.cozyspace.utils

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

enum class Category(
    val colors: CategoryColors
) {

    Kitchen(
        CategoryColors(
            dark = DarkYellowBubble,
            light = LightYellowBubble,
            transparent = TransparentYellowBubble
        )
    ),

    Work(
        CategoryColors(
            dark = DarkBlueBubble,
            light = LightBlueBubble,
            transparent = TransparentBlueBubble
        )
    ),

    Bedroom(
        CategoryColors(
            dark = DarkRedBubble,
            light = LightRedBubble,
            transparent = TransparentRedBubble
        )
    ),

    Garden(
        CategoryColors(
            dark = DarkGreenBubble,
            light = LightGreenBubble,
            transparent = TransparentGreenBubble
        )
    ),

    Bathroom(
        CategoryColors(
            dark = DarkPurpleBubble,
            light = LightPurpleBubble,
            transparent = TransparentPurpleBubble
        )
    )
}
