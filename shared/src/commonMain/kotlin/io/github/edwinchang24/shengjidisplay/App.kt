package io.github.edwinchang24.shengjidisplay

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.dialogs.AboutDialog
import io.github.edwinchang24.shengjidisplay.dialogs.EditCallDialog
import io.github.edwinchang24.shengjidisplay.dialogs.EditPossibleTrumpsDialog
import io.github.edwinchang24.shengjidisplay.dialogs.EditTrumpDialog
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.navigation.AndroidBackHandler
import io.github.edwinchang24.shengjidisplay.navigation.Dialog
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.navigation.Screen
import io.github.edwinchang24.shengjidisplay.pages.DisplayPage
import io.github.edwinchang24.shengjidisplay.pages.HomePage
import io.github.edwinchang24.shengjidisplay.pages.SettingsPage
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme
import io.github.edwinchang24.shengjidisplay.util.WindowSize
import io.github.edwinchang24.shengjidisplay.util.calculateWindowSize
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App(state: AppState.Prop, modifier: Modifier = Modifier) {
    ShengJiDisplayTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets(0))
        ) {
            val density = LocalDensity.current
            var currentScreen by
                rememberSaveable(stateSaver = Screen.Saver) { mutableStateOf(Screen.Home) }
            val settingsDragState =
                rememberSaveable(
                    saver =
                        AnchoredDraggableState.Saver(
                            positionalThreshold = { it * 0.5f },
                            velocityThreshold = { with(density) { 125.dp.toPx() } },
                            snapAnimationSpec = SpringSpec(),
                            decayAnimationSpec = exponentialDecay()
                        )
                ) {
                    AnchoredDraggableState(
                        false,
                        positionalThreshold = { it * 0.5f },
                        velocityThreshold = { with(density) { 125.dp.toPx() } },
                        snapAnimationSpec = SpringSpec(),
                        decayAnimationSpec = exponentialDecay()
                    )
                }
            var currentDialog by
                rememberSaveable(stateSaver = Dialog.Saver) { mutableStateOf(null) }
            val coroutineScope = rememberCoroutineScope()
            val navigator =
                object : Navigator {
                    override fun navigate(screen: Screen) {
                        currentScreen = screen
                    }

                    override fun toggleSettings() {
                        coroutineScope.launch {
                            settingsDragState.animateTo(!settingsDragState.targetValue)
                        }
                    }

                    override fun navigate(dialog: Dialog) {
                        currentDialog = dialog
                    }

                    override fun closeDialog() {
                        currentDialog = null
                    }
                }
            AndroidBackHandler(
                currentScreen,
                settingsDragState.targetValue,
                currentDialog,
                navigator
            )
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(tween(250, delayMillis = 250)) togetherWith fadeOut(tween(250))
                },
                modifier = Modifier.fillMaxSize()
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.Home -> HomePage(navigator, state)
                    is Screen.Display ->
                        DisplayPage(
                            targetScreen.scheme,
                            targetScreen.editTeammates,
                            navigator,
                            state
                        )
                }
            }
            SettingsPane(settingsDragState, navigator, state)
            AnimatedVisibility(
                currentDialog != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier =
                        Modifier.fillMaxSize()
                            .alpha(0.75f)
                            .background(MaterialTheme.colorScheme.surfaceDim)
                            .pointerInput(true) {
                                detectTapGestures { currentDialog = null }
                                detectDragGestures(
                                    onDragEnd = { currentDialog = null },
                                    onDragCancel = { currentDialog = null },
                                    onDrag = { _, _ -> }
                                )
                            }
                )
            }
            AnimatedContent(
                targetState = currentDialog,
                transitionSpec = {
                    fadeIn(tween(200)) + slideInVertically { it / 16 } togetherWith
                        fadeOut(tween(200)) + slideOutVertically { -it / 8 }
                },
                modifier = Modifier.fillMaxSize()
            ) { targetDialog ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (targetDialog != null) {
                        Card(
                            shape = MaterialTheme.shapes.large,
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
                        ) {
                            when (targetDialog) {
                                is Dialog.EditCall ->
                                    EditCallDialog(targetDialog.index, navigator, state)
                                Dialog.EditPossibleTrumps ->
                                    EditPossibleTrumpsDialog(navigator, state)
                                Dialog.EditTrump -> EditTrumpDialog(navigator, state)
                                Dialog.About -> AboutDialog(navigator)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SettingsPane(
    dragState: AnchoredDraggableState<Boolean>,
    navigator: Navigator,
    state: AppState.Prop
) {
    val coroutineScope = rememberCoroutineScope()
    val windowSize = calculateWindowSize()
    val closeSettings = { coroutineScope.launch { dragState.animateTo(false) } }
    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceDim.copy(
                        alpha =
                            0.75f *
                                (1f -
                                        (dragState.offset
                                            .takeIf { !it.isNaN() }
                                            ?.let { offset ->
                                                offset /
                                                    (dragState.anchors.positionOf(false).takeIf {
                                                        !it.isNaN()
                                                    } ?: offset)
                                            } ?: 1f))
                                    .coerceIn(0f..1f)
                    )
                )
                .then(
                    if (!dragState.currentValue) Modifier
                    else
                        Modifier.pointerInput(true) { detectTapGestures { closeSettings() } }
                            .anchoredDraggable(
                                state = dragState,
                                orientation = Orientation.Horizontal
                            )
                )
    )
    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            val width = with(LocalDensity.current) { maxWidth.toPx() }
            LaunchedEffect(width) {
                dragState.updateAnchors(
                    newAnchors =
                        DraggableAnchors {
                            true at 0f
                            false at width
                        }
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier =
                    (if (windowSize == WindowSize.Small) Modifier.fillMaxSize()
                        else Modifier.fillMaxHeight())
                        .then(
                            Modifier.offset {
                                    IntOffset(
                                        (dragState.offset.takeIf { !it.isNaN() } ?: Float.MAX_VALUE)
                                            .toInt(),
                                        0
                                    )
                                }
                                .anchoredDraggable(state = dragState, Orientation.Horizontal)
                        )
            ) {
                SettingsPage(navigator, state)
            }
        }
    }
}
