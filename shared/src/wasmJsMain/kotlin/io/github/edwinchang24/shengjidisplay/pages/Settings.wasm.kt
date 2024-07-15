package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.edwinchang24.shengjidisplay.model.AppState

@Composable
actual fun PlatformSettings(
    state: AppState.Prop,
    sectionHeader: @Composable (text: String, modifier: Modifier) -> Unit,
    booleanPicker:
        @Composable
        (value: Boolean, setValue: (Boolean) -> Unit, label: @Composable () -> Unit) -> Unit
) {}
