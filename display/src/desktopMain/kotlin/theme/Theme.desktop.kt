package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import model.AppState

@Composable
actual fun ShengJiDisplayTheme(state: AppState.Prop, content: @Composable () -> Unit) {
    val useDarkTheme = state().settings.general.theme.computesToDark()
    MaterialTheme(
        colorScheme = if (useDarkTheme) defaultDarkTheme else defaultLightTheme,
        typography = getTypography(Inter),
        content = content
    )
}
