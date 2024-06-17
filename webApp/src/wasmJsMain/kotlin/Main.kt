import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.edwinchang24.shengjidisplay.App
import io.github.edwinchang24.shengjidisplay.model.AppState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.configureWebResources

@OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)
fun main() {
    configureWebResources { resourcePathMapping { path -> "./$path" } }
    val appState = MutableStateFlow(AppState())
    CanvasBasedWindow(canvasElementId = "app") {
        val state by appState.collectAsState()
        App(state, { appState.value = it })
    }
}
