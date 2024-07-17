package io.github.edwinchang24.shengjidisplay.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import io.github.edwinchang24.shengjidisplay.model.AppState

@Composable
actual fun ShengJiDisplayTheme(state: AppState.Prop, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme =
            if (state().settings.general.theme.computesToDark()) darkColorScheme()
            else lightColorScheme(),
        typography = Typography,
        content = content
    )
}
