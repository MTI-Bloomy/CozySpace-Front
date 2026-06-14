package bloomy.cozyspace.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
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
        Font(Res.font.nunito_black),
        Font(Res.font.nunito_blackitalic),
        Font(Res.font.nunito_bold),
        Font(Res.font.nunito_bolditalic),
        Font(Res.font.nunito_extrabold),
        Font(Res.font.nunito_extrabolditalic),
        Font(Res.font.nunito_extralight),
        Font(Res.font.nunito_extralightitalic),
        Font(Res.font.nunito_italic),
        Font(Res.font.nunito_light),
        Font(Res.font.nunito_lightitalic),
        Font(Res.font.nunito_medium),
        Font(Res.font.nunito_mediumitalic),
        Font(Res.font.nunito_regular),
        Font(Res.font.nunito_semibold),
        Font(Res.font.nunito_semibolditalic),
    )
}
