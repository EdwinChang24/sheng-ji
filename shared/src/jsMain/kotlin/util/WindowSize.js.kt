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

private fun Window.size() = DpSize(innerWidth.dp, innerHeight.dp)

@Composable
actual fun calculateWindowWidth(): WindowWidth {
    var windowWidth by remember { mutableStateOf(WindowWidth.from(window.size())) }
    DisposableEffect(true) {
        val callback = { _: Event -> windowWidth = WindowWidth.from(window.size()) }
        window.addEventListener("resize", callback)
        onDispose { window.removeEventListener("resize", callback) }
    }
    return windowWidth
}

@Composable
actual fun calculateWindowHeight(): WindowHeight {
    var windowHeight by remember { mutableStateOf(WindowHeight.from(window.size())) }
    DisposableEffect(true) {
        val callback = { _: Event -> windowHeight = WindowHeight.from(window.size()) }
        window.addEventListener("resize", callback)
        onDispose { window.removeEventListener("resize", callback) }
    }
    return windowHeight
}
