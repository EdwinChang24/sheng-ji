import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import navigation.Screen
import org.w3c.dom.PopStateEvent
import org.w3c.dom.events.Event

@Composable
actual fun WebHistoryHandler(currentScreen: Screen, setCurrentScreen: (Screen) -> Unit) {
    LaunchedEffect(currentScreen) {
        val screen: Screen =
            try {
                Json.decodeFromString(JSON.stringify(window.history.state))
            } catch (_: IllegalArgumentException) {
                Screen.Home
            }
        if (screen != currentScreen) {
            window.history.pushState(JSON.parse(Json.encodeToString(currentScreen)), "")
        }
    }
    DisposableEffect(true) {
        val listener = { event: Event ->
            val newState = (event as? PopStateEvent)?.state
            val screen: Screen =
                try {
                    Json.decodeFromString(JSON.stringify(newState))
                } catch (_: IllegalArgumentException) {
                    currentScreen
                }
            setCurrentScreen(screen)
        }
        window.addEventListener("popstate", listener)
        onDispose { window.removeEventListener("popstate", listener) }
    }
}
