import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import arrow.optics.copy
import display.LocalWindowState
import java.io.File
import java.io.IOException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.AppState
import org.jetbrains.compose.resources.painterResource
import resources.Res
import resources.app_icon

val stateFile =
    if (System.getProperty("os.name").contains("win", ignoreCase = true)) {
        File(File(System.getenv("APPDATA"), "Sheng Ji Display"), "state.json")
    } else {
        File(File(System.getProperty("user.home"), ".sheng-ji-display"), "state.json")
    }

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val appState =
        MutableStateFlow(
            try {
                stateFile.readText().let { Json.decodeFromString(it) }
            } catch (e: Throwable) {
                when (e) {
                    is IllegalArgumentException,
                    is IOException,
                    is RuntimeException -> AppState()
                    else -> throw e
                }
            }
        )
    GlobalScope.launch(Dispatchers.IO) {
        appState.collectLatest { state ->
            delay(100)
            try {
                stateFile.parentFile.mkdirs()
                stateFile.writeText(Json.encodeToString(state))
            } catch (e: Throwable) {
                when (e) {
                    is IllegalArgumentException,
                    is IOException,
                    is RuntimeException -> {}
                    else -> throw e
                }
            }
        }
    }
    application {
        val windowState = rememberWindowState()
        val state by appState.collectAsState()
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "升级 Display $appVersion",
            icon = painterResource(Res.drawable.app_icon),
            onKeyEvent = { keyEvent ->
                if (keyEvent.key == Key.F11) {
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        windowState.placement =
                            if (windowState.placement == WindowPlacement.Fullscreen)
                                WindowPlacement.Floating
                            else WindowPlacement.Fullscreen
                        return@Window true
                    }
                }
                return@Window false
            },
        ) {
            CompositionLocalProvider(LocalWindowState provides windowState) {
                App(AppState.Prop(state) { copy -> appState.value = state.copy(copy) })
            }
        }
    }
}
