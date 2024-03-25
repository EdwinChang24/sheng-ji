package io.github.edwinchang24.shengjidisplay.pages

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.destinations.SettingsPageDestination
import io.github.edwinchang24.shengjidisplay.model.HorizontalOrientation
import io.github.edwinchang24.shengjidisplay.model.VerticalOrder
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

sealed interface DisplayContent {
    sealed interface Direction {
        data object Center : Direction
        data object Left : Direction
        data object Right : Direction

        fun opposite() = when (this) {
            Center -> Center
            Left -> Right
            Right -> Left
        }
    }

    data class Trump(val direction: Direction) : DisplayContent
    data class Calls(val direction: Direction) : DisplayContent
    data object None : DisplayContent
}

data class DisplaySettingsState(
    val autoHideCalls: Boolean,
    val verticalOrder: VerticalOrder,
    val perpendicularMode: Boolean,
    val horizontalOrientation: HorizontalOrientation,
    val autoSwitchSeconds: Int,
    val showCalls: Boolean
)

@SuppressLint("SourceLockedOrientationActivity")
@Destination(style = DisplayPageTransitions::class)
@MainNavGraph
@Composable
fun DisplayPage(
    navigator: DestinationsNavigator,
    mainActivityViewModel: MainActivityViewModel,
    displayViewModel: DisplayViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by mainActivityViewModel.state.collectAsStateWithLifecycle()
    val showCalls = !(state.settings.autoHideCalls && state.calls.all { it.found })
    val topContent by displayViewModel.topContent.collectAsStateWithLifecycle()
    val bottomContent by displayViewModel.bottomContent.collectAsStateWithLifecycle()
    var currentTimeMs by rememberSaveable { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }
    LaunchedEffect(state.settings, showCalls) {
        displayViewModel.onPotentialUpdate(
            DisplaySettingsState(
                state.settings.autoHideCalls, state.settings.verticalOrder, state.settings.perpendicularMode,
                state.settings.horizontalOrientation, state.settings.autoSwitchSeconds, showCalls
            )
        )
    }
    LaunchedEffect(true) {
        currentTimeMs = Clock.System.now().toEpochMilliseconds()
        while (true) {
            delay(60_000 - (currentTimeMs % 60_000))
            currentTimeMs = Clock.System.now().toEpochMilliseconds()
        }
    }
    fun Context.activity(): Activity? = this as? Activity ?: (this as? ContextWrapper)?.baseContext?.activity()
    DisposableEffect(state.settings.keepScreenOn) {
        context.activity()?.window.let { window ->
            if (state.settings.keepScreenOn) window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
        }
    }
    DisposableEffect(state.settings.lockScreenOrientation) {
        context.activity().let { activity ->
            if (state.settings.lockScreenOrientation) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            onDispose { activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED }
        }
    }
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
            ) {
                Text(topContent.toString())
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(16.dp)
            ) {
                val timeFormat = LocalDateTime.Format {
                    amPmHour()
                    char(':')
                    minute()
                }
                val clockText =
                    Instant.fromEpochMilliseconds(currentTimeMs).toLocalDateTime(TimeZone.currentSystemDefault())
                        .format(timeFormat)
                if (state.settings.showClock) AnimatedContent(
                    // @formatter:off
                    targetState = state.settings.clockOrientation,
                    label = "",
                    transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut() },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            mainActivityViewModel.state.value = state.copy(
                                settings = state.settings.copy(clockOrientation = !state.settings.clockOrientation)
                            )
                        }
                        .padding(8.dp)
                    // @formatter:on
                ) { targetOrientation ->
                    Text(
                        clockText, style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.rotate(if (targetOrientation) 0f else 180f)
                    )
                }
                IconButton(onClick = { navigator.navigate(SettingsPageDestination) }) {
                    Icon(painterResource(R.drawable.ic_settings), null)
                }
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(painterResource(R.drawable.ic_close), null)
                }
                if (state.settings.showClock) AnimatedContent(
                    // @formatter:off
                    targetState = state.settings.clockOrientation,
                    label = "",
                    transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut() },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            mainActivityViewModel.state.value = state.copy(
                                settings = state.settings.copy(clockOrientation = !state.settings.clockOrientation)
                            )
                        }
                        .padding(8.dp)
                    // @formatter:on
                ) { targetOrientation ->
                    Text(
                        clockText, style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.rotate(if (targetOrientation) 180f else 0f)
                    )
                }
            }
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
            ) {
                Text(bottomContent.toString())
            }
        }
    }
}

object DisplayPageTransitions : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() = fadeIn()
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() = fadeOut()
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition() = fadeIn()
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() = fadeOut()
}
