package navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun AndroidBackHandler(
    currentScreen: Screen,
    settingsOpen: Boolean,
    currentDialog: Dialog?,
    navigator: Navigator,
) {
    BackHandler {
        if (currentDialog != null) {
            navigator.closeDialog()
        } else if (settingsOpen) {
            navigator.toggleSettings()
        } else if (currentScreen is Screen.Display) {
            navigator.navigate(Screen.Home)
        }
    }
}
