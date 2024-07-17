package io.github.edwinchang24.shengjidisplay.theme

import androidx.compose.runtime.Composable
import io.github.edwinchang24.shengjidisplay.model.AppState

@Composable expect fun ShengJiDisplayTheme(state: AppState.Prop, content: @Composable () -> Unit)
