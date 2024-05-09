package io.github.edwinchang24.shengjidisplay.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun TeammatesBackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled, onBack)
