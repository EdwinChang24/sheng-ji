package navigation

import androidx.compose.runtime.Composable

@Composable
actual fun AndroidBackHandler(
    currentScreen: Screen,
    settingsOpen: Boolean,
    currentDialog: Dialog?,
    navigator: Navigator,
) {}
