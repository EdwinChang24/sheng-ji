package dev.edwinchang.shengjidisplay

import App
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import arrow.optics.copy
import model.AppState
import transfer.decodeParam
import transfer.toUrl

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val importUrl =
            if (intent.action == Intent.ACTION_VIEW) {
                intent.data?.let { uri ->
                    uri.path
                        ?.takeIf { it != "/display" }
                        ?.removePrefix("/")
                        ?.let { decodeParam(it) }
                        ?.takeIf { !it.isEmpty() }
                        ?.toUrl()
                }
            } else null
        viewModel.load(this)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            App(
                state = AppState.Prop(state) { copy -> viewModel.state.value = state.copy(copy) },
                importUrl = importUrl,
            )
        }
    }
}
