package bloomy.cozyspace.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import cozyspace.composeapp.generated.resources.Res
import cozyspace.composeapp.generated.resources.nunito_black
import cozyspace.composeapp.generated.resources.nunito_blackitalic
import cozyspace.composeapp.generated.resources.nunito_bold
import cozyspace.composeapp.generated.resources.nunito_bolditalic
import cozyspace.composeapp.generated.resources.nunito_extrabold
import cozyspace.composeapp.generated.resources.nunito_extrabolditalic
import cozyspace.composeapp.generated.resources.nunito_extralight
import cozyspace.composeapp.generated.resources.nunito_extralightitalic
import cozyspace.composeapp.generated.resources.nunito_italic
import cozyspace.composeapp.generated.resources.nunito_light
import cozyspace.composeapp.generated.resources.nunito_lightitalic
import cozyspace.composeapp.generated.resources.nunito_medium
import cozyspace.composeapp.generated.resources.nunito_mediumitalic
import cozyspace.composeapp.generated.resources.nunito_regular
import cozyspace.composeapp.generated.resources.nunito_semibold
import cozyspace.composeapp.generated.resources.nunito_semibolditalic
import org.jetbrains.compose.resources.Font

@Composable
fun appFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.nunito_black, weight = FontWeight.Black),
        Font(Res.font.nunito_blackitalic, weight = FontWeight.Black, style = FontStyle.Italic),
        Font(Res.font.nunito_bold, weight = FontWeight.Bold),
        Font(Res.font.nunito_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(Res.font.nunito_extrabold, weight = FontWeight.ExtraBold),
        Font(Res.font.nunito_extrabolditalic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
        Font(Res.font.nunito_extralight, weight = FontWeight.ExtraLight),
        Font(Res.font.nunito_extralightitalic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
        Font(Res.font.nunito_italic, style = FontStyle.Italic),
        Font(Res.font.nunito_light, weight = FontWeight.Light),
        Font(Res.font.nunito_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
        Font(Res.font.nunito_medium, weight = FontWeight.Medium),
        Font(Res.font.nunito_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
        Font(Res.font.nunito_regular, weight = FontWeight.Normal),
        Font(Res.font.nunito_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.nunito_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    )
}
