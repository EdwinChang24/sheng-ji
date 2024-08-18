package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import model.AppState

@Composable
actual fun ShengJiDisplayTheme(state: AppState.Prop, content: @Composable () -> Unit) {
    val useDarkTheme = state().settings.general.theme.computesToDark()
    MaterialTheme(
        colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(),
        typography = getTypography(Inter),
        content = content
    )
}
