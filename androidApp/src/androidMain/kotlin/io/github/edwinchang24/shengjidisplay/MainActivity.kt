package io.github.edwinchang24.shengjidisplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.load(this)
        setContent {
            val state by
                viewModel.state.collectAsStateWithLifecycle(
                    lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
                )
            App(state, setState = { viewModel.state.value = it })
        }
    }
}
