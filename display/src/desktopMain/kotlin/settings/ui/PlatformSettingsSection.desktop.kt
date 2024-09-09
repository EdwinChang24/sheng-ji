package settings.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import model.AppState
import model.platformSettings
import settings.desktop
import settings.fullScreen
import settings.invoke

@Composable
actual fun PlatformSettingsSection(
    state: AppState.Prop,
    sectionHeader: @Composable (text: String, modifier: Modifier) -> Unit,
    booleanPicker:
        @Composable
        (value: Boolean, setValue: (Boolean) -> Unit, label: @Composable () -> Unit) -> Unit,
) {
    sectionHeader("Desktop settings", Modifier)
    booleanPicker(
        state().platformSettings().fullScreen,
        { state { AppState.platformSettings.desktop.fullScreen set it } },
    ) {
        Text("Full screen")
    }
}
