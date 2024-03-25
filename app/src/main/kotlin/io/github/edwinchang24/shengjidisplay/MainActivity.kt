package io.github.edwinchang24.shengjidisplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.load(this)
        setContent {
            ShengJiDisplayTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.main, dependenciesContainerBuilder = { dependency(viewModel) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
