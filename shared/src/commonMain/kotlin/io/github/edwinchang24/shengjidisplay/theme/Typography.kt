package io.github.edwinchang24.shengjidisplay.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.inter_black
import io.github.edwinchang24.shengjidisplay.resources.inter_bold
import io.github.edwinchang24.shengjidisplay.resources.inter_extrabold
import io.github.edwinchang24.shengjidisplay.resources.inter_extralight
import io.github.edwinchang24.shengjidisplay.resources.inter_light
import io.github.edwinchang24.shengjidisplay.resources.inter_medium
import io.github.edwinchang24.shengjidisplay.resources.inter_regular
import io.github.edwinchang24.shengjidisplay.resources.inter_semibold
import io.github.edwinchang24.shengjidisplay.resources.inter_thin
import org.jetbrains.compose.resources.Font

private val Inter
    @Composable
    get() =
        FontFamily(
            Font(Res.font.inter_thin, weight = FontWeight.Thin),
            Font(Res.font.inter_extralight, weight = FontWeight.ExtraLight),
            Font(Res.font.inter_light, weight = FontWeight.Light),
            Font(Res.font.inter_regular, weight = FontWeight.Normal),
            Font(Res.font.inter_medium, weight = FontWeight.Medium),
            Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
            Font(Res.font.inter_bold, weight = FontWeight.Bold),
            Font(Res.font.inter_extrabold, weight = FontWeight.ExtraBold),
            Font(Res.font.inter_black, weight = FontWeight.Black),
        )

val Typography
    @Composable
    get() =
        with(Typography()) {
            Typography(
                displayLarge = displayLarge.copy(fontFamily = Inter, fontWeight = FontWeight.Bold),
                displayMedium =
                    displayMedium.copy(fontFamily = Inter, fontWeight = FontWeight.Bold),
                displaySmall = displaySmall.copy(fontFamily = Inter, fontWeight = FontWeight.Bold),
                headlineLarge =
                    headlineLarge.copy(fontFamily = Inter, fontWeight = FontWeight.SemiBold),
                headlineMedium =
                    headlineMedium.copy(fontFamily = Inter, fontWeight = FontWeight.SemiBold),
                headlineSmall =
                    headlineSmall.copy(fontFamily = Inter, fontWeight = FontWeight.SemiBold),
                titleLarge = titleLarge.copy(fontFamily = Inter, fontWeight = FontWeight.Medium),
                titleMedium = titleMedium.copy(fontFamily = Inter, fontWeight = FontWeight.Medium),
                titleSmall = titleSmall.copy(fontFamily = Inter, fontWeight = FontWeight.Medium),
                bodyLarge = bodyLarge.copy(fontFamily = Inter, fontWeight = FontWeight.Normal),
                bodyMedium = bodyMedium.copy(fontFamily = Inter, fontWeight = FontWeight.Normal),
                bodySmall = bodySmall.copy(fontFamily = Inter, fontWeight = FontWeight.Normal),
                labelLarge = labelLarge.copy(fontFamily = Inter, fontWeight = FontWeight.Medium),
                labelMedium = labelMedium.copy(fontFamily = Inter, fontWeight = FontWeight.Medium),
                labelSmall = labelSmall.copy(fontFamily = Inter, fontWeight = FontWeight.Medium)
            )
        }
