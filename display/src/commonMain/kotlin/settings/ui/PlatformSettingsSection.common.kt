package settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import model.AppState

@Composable
expect fun PlatformSettingsSection(
    state: AppState.Prop,
    sectionHeader: @Composable (text: String, modifier: Modifier) -> Unit,
    booleanPicker:
        @Composable
        (value: Boolean, setValue: (Boolean) -> Unit, label: @Composable () -> Unit) -> Unit,
)
