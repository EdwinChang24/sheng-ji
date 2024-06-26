import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.edwinchang24.shengjidisplay.App
import io.github.edwinchang24.shengjidisplay.model.AppState
import kotlinx.browser.localStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.configureWebResources

@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
fun main() {
    configureWebResources { resourcePathMapping { path -> "./$path" } }
    val storageKey = "state"
    val appState =
        MutableStateFlow(
            localStorage.getItem(storageKey)?.let { data -> Json.decodeFromString(data) }
                ?: AppState()
        )
    GlobalScope.launch {
        appState.collectLatest { state ->
            delay(100)
            localStorage.setItem(storageKey, Json.encodeToString(state))
        }
    }
    CanvasBasedWindow(canvasElementId = "app") {
        val state by appState.collectAsState()
        App(state, { appState.value = it })
    }
}
