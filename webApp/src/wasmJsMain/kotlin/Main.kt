import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import arrow.optics.copy
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.AppState
import org.jetbrains.compose.resources.configureWebResources
import org.w3c.dom.StorageEvent

@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
fun main() {
    configureWebResources { resourcePathMapping { path -> "./$path" } }
    val stateStorageKey = "shengjidisplay.state"
    val themeStorageKey = "shengjidisplay.theme"
    val appState =
        MutableStateFlow(
            localStorage.getItem(stateStorageKey)?.let { data ->
                try {
                    Json.decodeFromString(data)
                } catch (e: IllegalArgumentException) {
                    AppState()
                }
            } ?: AppState()
        )
    window.addEventListener("storage") { e ->
        (e as? StorageEvent)?.let {
            if (it.key == stateStorageKey)
                appState.value =
                    localStorage.getItem(stateStorageKey)?.let { data ->
                        try {
                            Json.decodeFromString(data)
                        } catch (e: IllegalArgumentException) {
                            AppState()
                        }
                    } ?: AppState()
            else if (it.key == themeStorageKey) updateTheme()
        }
    }
    GlobalScope.launch {
        appState.collectLatest { state ->
            localStorage.setItem(themeStorageKey, state.settings.general.theme.name)
            localStorage.setItem(stateStorageKey, Json.encodeToString(state))
            updateTheme()
        }
    }
    CanvasBasedWindow(canvasElementId = "app") {
        val state by appState.collectAsState()
        App(AppState.Prop(state) { copy -> appState.value = state.copy(copy) })
    }
}

external fun updateTheme()
