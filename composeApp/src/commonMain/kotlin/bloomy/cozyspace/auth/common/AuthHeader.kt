package bloomy.cozyspace.auth.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bloomy.cozyspace.theme.DarkGreen
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.cozyspace_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthHeader(title: String, imageWidth: Dp) {
    Image(
        painter = painterResource(Res.drawable.cozyspace_logo),
        contentDescription = "CozySpace Logo",
        contentScale = ContentScale.Inside,
        modifier = Modifier
            .width(imageWidth)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(30.dp))
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(title,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = DarkGreen,
        textAlign = TextAlign.Center
    )
}
