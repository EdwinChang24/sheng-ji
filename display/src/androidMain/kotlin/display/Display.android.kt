package display

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import model.AppState
import settings.invoke

fun Context.activity(): Activity? =
    this as? Activity ?: (this as? ContextWrapper)?.baseContext?.activity()

@Composable
actual fun KeepScreenOn(state: AppState.Prop) {
    val context = LocalContext.current
    val enabled = state().platformSettings().keepScreenOn
    DisposableEffect(enabled) {
        context.activity()?.window.let { window ->
            if (enabled) window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            else window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
        }
    }
}

@Composable
actual fun LockScreenOrientation(state: AppState.Prop) {
    val context = LocalContext.current
    val enabled = state().platformSettings().lockOrientation
    DisposableEffect(enabled) {
        context.activity().let { activity ->
            activity?.requestedOrientation =
                if (enabled) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            onDispose {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
}

@Composable
actual fun FullScreen(state: AppState.Prop) {
    val view = LocalView.current
    val context = LocalContext.current
    val enabled = state().platformSettings().fullScreen
    DisposableEffect(enabled) {
        context.activity()?.window?.let { window ->
            WindowCompat.getInsetsController(window, view).run {
                if (enabled) {
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    hide(WindowInsetsCompat.Type.systemBars())
                } else {
                    show(WindowInsetsCompat.Type.systemBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        }
        onDispose {
            context.activity()?.window?.let { window ->
                WindowCompat.getInsetsController(window, view).run {
                    show(WindowInsetsCompat.Type.systemBars())
                    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                }
            }
        }
    }
}
