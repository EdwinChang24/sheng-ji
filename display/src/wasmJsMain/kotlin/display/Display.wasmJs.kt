package display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlin.js.Promise
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import model.AppState
import settings.invoke

private fun requestWakeLock(): JsAny = js("navigator.wakeLock.request(\"screen\")")

private fun releaseWakeLock(@Suppress("UNUSED_PARAMETER") sentinel: JsAny): JsAny =
    js("sentinel.release()")

@Composable
actual fun KeepScreenOn(state: AppState.Prop) {
    val coroutineScope = rememberCoroutineScope()
    val enabled = state().platformSettings().keepScreenOn
    var sentinel: JsAny? = null
    DisposableEffect(enabled) {
        coroutineScope.launch {
            sentinel?.let { (releaseWakeLock(it) as? Promise<*>)?.await<Unit>() }
            sentinel = if (enabled) (requestWakeLock() as? Promise<*>)?.await() else null
        }
        onDispose {
            coroutineScope.launch {
                sentinel?.let { (releaseWakeLock(it) as? Promise<*>)?.await<Unit>() }
                sentinel = null
            }
        }
    }
}

@Composable actual fun LockScreenOrientation(state: AppState.Prop) {}

@Composable actual fun FullScreen(state: AppState.Prop) {}
