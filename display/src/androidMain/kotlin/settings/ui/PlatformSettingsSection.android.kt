package settings.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import model.AppState
import model.platformSettings
import settings.android
import settings.fullScreen
import settings.invoke
import settings.keepScreenOn
import settings.lockOrientation

@Composable
actual fun PlatformSettingsSection(
    state: AppState.Prop,
    sectionHeader: @Composable (text: String, modifier: Modifier) -> Unit,
    booleanPicker:
        @Composable
        (value: Boolean, setValue: (Boolean) -> Unit, label: @Composable () -> Unit) -> Unit
) {
    sectionHeader("Android settings", Modifier)
    booleanPicker(
        state().platformSettings().fullScreen,
        { state { AppState.platformSettings.android.fullScreen set it } }
    ) {
        Text("Full screen")
    }
    booleanPicker(
        state().platformSettings().lockOrientation,
        { state { AppState.platformSettings.android.lockOrientation set it } }
    ) {
        Text("Lock screen orientation to portrait")
    }
    booleanPicker(
        state().platformSettings().keepScreenOn,
        { state { AppState.platformSettings.android.keepScreenOn set it } }
    ) {
        Text("Keep screen on")
    }
}
