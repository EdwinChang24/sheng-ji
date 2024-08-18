package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.AppState
import resources.Res

@OptIn(DelicateCoroutinesApi::class)
@Composable
actual fun ShengJiDisplayTheme(state: AppState.Prop, content: @Composable () -> Unit) {
    var fontFamily: FontFamily? by remember { mutableStateOf(null) }
    var fontFamilyLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        if (!fontFamilyLoaded) {
            GlobalScope.launch { fontFamily = getInterWeb() }
            fontFamilyLoaded = true
        }
    }
    MaterialTheme(
        colorScheme =
            if (state().settings.general.theme.computesToDark()) darkColorScheme()
            else lightColorScheme(),
        typography = getTypography(fontFamily ?: FontFamily.SansSerif),
        content = content
    )
}

suspend fun getInterWeb(): FontFamily {
    suspend operator fun String.invoke() = Res.readBytes("font/$this")
    return FontFamily(
        Font("inter_regular", "inter_regular.otf"(), weight = FontWeight.Normal),
        Font("inter_bold", "inter_bold.otf"(), weight = FontWeight.Bold),
        Font("inter_semibold", "inter_semibold.otf"(), weight = FontWeight.SemiBold),
        Font("inter_medium", "inter_medium.otf"(), weight = FontWeight.Medium),
        Font("inter_thin", "inter_thin.otf"(), weight = FontWeight.Thin),
        Font("inter_extralight", "inter_extralight.otf"(), weight = FontWeight.ExtraLight),
        Font("inter_light", "inter_light.otf"(), weight = FontWeight.Light),
        Font("inter_extrabold", "inter_extrabold.otf"(), weight = FontWeight.ExtraBold),
        Font("inter_black", "inter_black.otf"(), weight = FontWeight.Black)
    )
}
