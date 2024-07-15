package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.android
import io.github.edwinchang24.shengjidisplay.model.fullScreen
import io.github.edwinchang24.shengjidisplay.model.invoke
import io.github.edwinchang24.shengjidisplay.model.keepScreenOn
import io.github.edwinchang24.shengjidisplay.model.lockOrientation
import io.github.edwinchang24.shengjidisplay.model.platformSettings

@Composable
actual fun PlatformSettings(
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
