package display

import androidx.compose.runtime.Composable
import model.AppState

@Composable actual fun KeepScreenOn(state: AppState.Prop) {}

@Composable actual fun LockScreenOrientation(state: AppState.Prop) {}

@Composable actual fun FullScreen(state: AppState.Prop) {}
