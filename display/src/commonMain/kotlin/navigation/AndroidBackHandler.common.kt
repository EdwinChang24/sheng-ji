package navigation

import androidx.compose.runtime.Composable

@Composable
expect fun AndroidBackHandler(
    currentScreen: Screen,
    settingsOpen: Boolean,
    currentDialog: Dialog?,
    navigator: Navigator
)
