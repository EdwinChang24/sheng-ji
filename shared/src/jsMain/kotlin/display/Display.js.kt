package display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlin.js.Promise
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import model.AppState
import settings.invoke

private fun requestWakeLock(): dynamic = js("navigator.wakeLock.request(\"screen\")")

private fun releaseWakeLock(@Suppress("UNUSED_PARAMETER") sentinel: dynamic): dynamic =
    js("sentinel.release()")

@Composable
actual fun KeepScreenOn(state: AppState.Prop) {
    val coroutineScope = rememberCoroutineScope()
    val enabled = state().platformSettings().keepScreenOn
    var sentinel: dynamic = null
    DisposableEffect(enabled) {
        coroutineScope.launch {
            if (sentinel != null) (releaseWakeLock(sentinel) as? Promise<*>)?.await()
            sentinel = if (enabled) (requestWakeLock() as? Promise<*>)?.await() else null
        }
        onDispose {
            coroutineScope.launch {
                if (sentinel != null) (releaseWakeLock(sentinel) as? Promise<*>)?.await()
                sentinel = null
            }
        }
    }
}

@Composable actual fun LockScreenOrientation(state: AppState.Prop) {}

@Composable actual fun FullScreen(state: AppState.Prop) {}
