package display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import model.AppState
import settings.invoke

@Composable actual fun KeepScreenOn(state: AppState.Prop) {}

@Composable actual fun LockScreenOrientation(state: AppState.Prop) {}

@Composable
actual fun FullScreen(state: AppState.Prop) {
    val windowState = LocalWindowState.current
    val enabled = state().platformSettings().fullScreen
    var lastPlacement: WindowPlacement? by remember { mutableStateOf(null) }
    var previouslyEnabled by remember { mutableStateOf(enabled) }
    DisposableEffect(enabled) {
        if (enabled) {
            lastPlacement = windowState.placement
            windowState.placement = WindowPlacement.Fullscreen
        } else if (previouslyEnabled) windowState.placement = WindowPlacement.Floating
        previouslyEnabled = enabled
        onDispose { if (enabled) windowState.placement = WindowPlacement.Floating }
    }
}

val LocalWindowState = compositionLocalOf<WindowState> { error("Window state not provided") }
