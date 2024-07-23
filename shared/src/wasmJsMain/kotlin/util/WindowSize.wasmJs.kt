package util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import org.w3c.dom.Window
import org.w3c.dom.events.Event

@Composable
actual fun calculateWindowSize(): WindowSize {
    fun Window.size() = DpSize(innerWidth.dp, innerHeight.dp)
    var windowSize by remember { mutableStateOf(WindowSize.from(window.size())) }
    DisposableEffect(true) {
        val callback = { _: Event -> windowSize = WindowSize.from(window.size()) }
        window.addEventListener("resize", callback)
        onDispose { window.removeEventListener("resize", callback) }
    }
    return windowSize
}
