package bloomy.cozyspace.todoList.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class CornerTriangleShape(private val cornerRadius: Dp = 0.dp, private val mirrored: Boolean = false) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val radiusPx = with(density) { cornerRadius.toPx() }

        // Returns the symmetry of x along the vertical axe
        fun mx(x: Float) = if (!mirrored) size.width - x else x

        val path = Path().apply {
            moveTo(mx(size.width - radiusPx), 0f)                             // Start of shape top left
            quadraticTo(mx(size.width), 0f, mx(size.width), radiusPx) // Round corner
            lineTo(mx(size.width), size.height)                               // From top right to bottom right
            lineTo(mx(0f), 0f)                                                // Diagonal
            close()
        }
        return Outline.Generic(path)
    }
}
