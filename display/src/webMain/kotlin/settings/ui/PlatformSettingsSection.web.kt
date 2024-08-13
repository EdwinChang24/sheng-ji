package settings.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import model.AppState
import model.platformSettings
import settings.importInto
import settings.invoke
import settings.keepScreenOn
import settings.web

@Composable
actual fun PlatformSettingsSection(
    state: AppState.Prop,
    sectionHeader: @Composable (text: String, modifier: Modifier) -> Unit,
    booleanPicker:
        @Composable
        (value: Boolean, setValue: (Boolean) -> Unit, label: @Composable () -> Unit) -> Unit
) {
    sectionHeader("Web settings", Modifier)
    booleanPicker(
        state().platformSettings().keepScreenOn,
        { state { AppState.platformSettings.web.keepScreenOn set it } }
    ) {
        Text("Keep screen on")
    }
    SettingsImportIntoPicker(
        importInto = state().platformSettings().importInto,
        setImportInto = { state { AppState.platformSettings.web.importInto set it } }
    )
}
