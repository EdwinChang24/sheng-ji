package io.github.edwinchang24.shengjidisplay.pages

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import kotlin.time.Duration.Companion.seconds

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

@Destination(style = DisplayPageTransitions::class)
@MainNavGraph
@Composable
fun DisplayPage(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var topContent: DisplayContent by remember { mutableStateOf(DisplayContent.None) }
    var bottomContent: DisplayContent by remember { mutableStateOf(DisplayContent.None) }
    val showCalls = !(state.settings.autoHideCalls && state.calls.all { it.found })
    var currentTimeMs by rememberSaveable { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }
    LaunchedEffect(state.settings, showCalls) {
        with(state.settings) {
            val topDirection = if (perpendicularMode) when (horizontalOrientation) {
                HorizontalOrientation.Auto, HorizontalOrientation.TopTowardsRight -> DisplayContent.Direction.Right
                HorizontalOrientation.BottomTowardsRight -> DisplayContent.Direction.Left
            } else DisplayContent.Direction.Center
            topContent = if (!showCalls) DisplayContent.Trump(topDirection) else when (verticalOrder) {
                VerticalOrder.Auto, VerticalOrder.TrumpOnTop -> DisplayContent.Trump(topDirection)
                VerticalOrder.CallsOnTop -> DisplayContent.Calls(topDirection)
            }
            val bottomDirection = if (perpendicularMode) when (horizontalOrientation) {
                HorizontalOrientation.Auto, HorizontalOrientation.TopTowardsRight -> DisplayContent.Direction.Left
                HorizontalOrientation.BottomTowardsRight -> DisplayContent.Direction.Right
            } else DisplayContent.Direction.Center
            bottomContent = if (!showCalls) DisplayContent.Trump(bottomDirection) else when (verticalOrder) {
                VerticalOrder.Auto, VerticalOrder.TrumpOnTop -> DisplayContent.Calls(bottomDirection)
                VerticalOrder.CallsOnTop -> DisplayContent.Trump(bottomDirection)
            }
            if (verticalOrder == VerticalOrder.Auto || (perpendicularMode && horizontalOrientation == HorizontalOrientation.Auto)) {
                while (true) {
                    delay(autoSwitchSeconds.seconds)
                    if (showCalls) when {
                        verticalOrder == VerticalOrder.Auto && horizontalOrientation == HorizontalOrientation.Auto -> {
                            when (val tc = topContent) {
                                is DisplayContent.Trump -> when (tc.direction) {
                                    DisplayContent.Direction.Center -> {
                                        topContent = DisplayContent.Calls(DisplayContent.Direction.Center)
                                        bottomContent = DisplayContent.Trump(DisplayContent.Direction.Center)
                                    }

                                    DisplayContent.Direction.Left -> {
                                        topContent = DisplayContent.Calls(DisplayContent.Direction.Right)
                                        bottomContent = DisplayContent.Trump(DisplayContent.Direction.Left)
                                    }

                                    DisplayContent.Direction.Right -> {
                                        topContent = DisplayContent.Calls(DisplayContent.Direction.Left)
                                        bottomContent = DisplayContent.Trump(DisplayContent.Direction.Right)
                                    }
                                }

                                is DisplayContent.Calls -> when (tc.direction) {
                                    DisplayContent.Direction.Center -> {
                                        topContent = DisplayContent.Trump(DisplayContent.Direction.Center)
                                        bottomContent = DisplayContent.Calls(DisplayContent.Direction.Center)
                                    }

                                    DisplayContent.Direction.Left -> {
                                        topContent = DisplayContent.Trump(DisplayContent.Direction.Left)
                                        bottomContent = DisplayContent.Calls(DisplayContent.Direction.Right)
                                    }

                                    DisplayContent.Direction.Right -> {
                                        topContent = DisplayContent.Trump(DisplayContent.Direction.Right)
                                        bottomContent = DisplayContent.Calls(DisplayContent.Direction.Left)
                                    }
                                }

                                DisplayContent.None -> error("Should've initialized topContent")
                            }
                        }

                        verticalOrder == VerticalOrder.Auto -> {
                            when (val tc = topContent) {
                                is DisplayContent.Trump -> {
                                    topContent = DisplayContent.Calls(tc.direction)
                                    bottomContent = DisplayContent.Trump(tc.direction.opposite())
                                }

                                is DisplayContent.Calls -> {
                                    topContent = DisplayContent.Trump(tc.direction)
                                    bottomContent = DisplayContent.Calls(tc.direction.opposite())
                                }

                                DisplayContent.None -> error("Should've initialized topContent")
                            }
                        }

                        horizontalOrientation == HorizontalOrientation.Auto -> {
                            when (val tc = topContent) {
                                is DisplayContent.Trump -> {
                                    topContent = DisplayContent.Trump(tc.direction.opposite())
                                    bottomContent = DisplayContent.Calls(tc.direction)
                                }

                                is DisplayContent.Calls -> {
                                    topContent = DisplayContent.Calls(tc.direction.opposite())
                                    bottomContent = DisplayContent.Trump(tc.direction)
                                }

                                DisplayContent.None -> error("Should've initialized topContent")
                            }
                        }
                    } else {
                        val tc = topContent as? DisplayContent.Trump ?: error("topContent should be Trump")
                        if (horizontalOrientation == HorizontalOrientation.Auto) {
                            topContent = DisplayContent.Trump(tc.direction.opposite())
                            bottomContent = DisplayContent.Trump(tc.direction)
                        } else {
                            topContent = DisplayContent.Trump(tc.direction)
                            bottomContent = DisplayContent.Trump(tc.direction.opposite())
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(true) {
        currentTimeMs = Clock.System.now().toEpochMilliseconds()
        while (true) {
            delay(60_000 - (currentTimeMs % 60_000))
            currentTimeMs = Clock.System.now().toEpochMilliseconds()
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
                            viewModel.state.value = state.copy(
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
                            viewModel.state.value = state.copy(
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
