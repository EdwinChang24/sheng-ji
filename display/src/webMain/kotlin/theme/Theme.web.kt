package theme

import androidx.compose.material3.MaterialTheme
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
            if (state().settings.general.theme.computesToDark()) defaultDarkTheme
            else defaultLightTheme,
        typography = getTypography(fontFamily ?: FontFamily.SansSerif),
        content = content,
    )
}

suspend fun getInterWeb(): FontFamily {
    suspend operator fun String.invoke() = Res.readBytes("font/$this")
    val regular = "inter_regular.ttf"()
    val bold = "inter_bold.ttf"()
    val semibold = "inter_semibold.ttf"()
    val medium = "inter_medium.ttf"()
    return FontFamily(
        Font("inter_regular", regular, weight = FontWeight.Normal),
        Font("inter_bold", bold, weight = FontWeight.Bold),
        Font("inter_semibold", semibold, weight = FontWeight.SemiBold),
        Font("inter_medium", medium, weight = FontWeight.Medium),
        Font("inter_thin", regular, weight = FontWeight.Thin),
        Font("inter_extralight", regular, weight = FontWeight.ExtraLight),
        Font("inter_light", regular, weight = FontWeight.Light),
        Font("inter_extrabold", bold, weight = FontWeight.ExtraBold),
        Font("inter_black", bold, weight = FontWeight.Black),
    )
}
