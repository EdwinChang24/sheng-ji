package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.edwinchang24.shengjidisplay.components.ActionMenu
import io.github.edwinchang24.shengjidisplay.components.ActionMenuButtonSize
import io.github.edwinchang24.shengjidisplay.components.IconButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.Scale
import io.github.edwinchang24.shengjidisplay.components.Teammates
import io.github.edwinchang24.shengjidisplay.display.CallsDisplay
import io.github.edwinchang24.shengjidisplay.display.ContentRotation
import io.github.edwinchang24.shengjidisplay.display.DisplayContent
import io.github.edwinchang24.shengjidisplay.display.DisplayContentWithRotation
import io.github.edwinchang24.shengjidisplay.display.DisplayScheme
import io.github.edwinchang24.shengjidisplay.display.PossibleTrumpsDisplay
import io.github.edwinchang24.shengjidisplay.display.TrumpDisplay
import io.github.edwinchang24.shengjidisplay.model.Action
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.clockOrientation
import io.github.edwinchang24.shengjidisplay.model.displayRotationVertical
import io.github.edwinchang24.shengjidisplay.model.general
import io.github.edwinchang24.shengjidisplay.model.settings
import io.github.edwinchang24.shengjidisplay.model.teammates
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.navigation.Screen
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_close
import io.github.edwinchang24.shengjidisplay.resources.ic_group
import io.github.edwinchang24.shengjidisplay.util.WindowSize
import io.github.edwinchang24.shengjidisplay.util.calculateWindowSize
import io.github.edwinchang24.shengjidisplay.util.iconRes
import io.github.edwinchang24.shengjidisplay.util.rotate90
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun DisplayPage(
    displayScheme: DisplayScheme,
    editTeammates: Boolean = false,
    navigator: Navigator,
    state: AppState.Prop,
    displayViewModel: DisplayViewModel =
        viewModel(key = Json.encodeToString(displayScheme)) { DisplayViewModel() }
) {
    val windowSize = calculateWindowSize()
    val content by displayViewModel.currentContent.collectAsStateWithLifecycle()
    var currentTimeMs by rememberSaveable {
        mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
    }
    var editingTeammates by rememberSaveable { mutableStateOf(editTeammates) }
    var editingScale by rememberSaveable { mutableStateOf(false) }
    var pause by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(
        displayScheme.getPossibleContentPairs(state()),
        state().settings.general.contentRotation.possibleRotations,
        pause,
        state().settings.general.autoSwitchSeconds
    ) {
        displayViewModel.onStateUpdate(
            newPossibleContentPairs = displayScheme.getPossibleContentPairs(state()),
            newPossibleRotations = state().settings.general.contentRotation.possibleRotations,
            newPause = pause,
            newAutoSwitchSeconds = state().settings.general.autoSwitchSeconds
        )
    }
    LaunchedEffect(true) {
        currentTimeMs = Clock.System.now().toEpochMilliseconds()
        while (true) {
            delay(60_000 - (currentTimeMs % 60_000))
            currentTimeMs = Clock.System.now().toEpochMilliseconds()
        }
    }
    KeepScreenOn(state)
    LockScreenOrientation(state)
    FullScreen(state)
    AnimatedContent(
        targetState = state().settings.general.displayRotationVertical,
        transitionSpec = { fadeIn(tween(200, delayMillis = 200)) togetherWith fadeOut(tween(200)) },
        modifier = Modifier.fillMaxSize()
    ) { displayRotationVertical ->
        if (displayRotationVertical) {
            DisplayVertical(
                content,
                displayScheme,
                navigator,
                state,
                windowSize,
                currentTimeMs,
                { editingTeammates = true }
            )
        } else {
            DisplayHorizontal(
                content,
                displayScheme,
                navigator,
                state,
                currentTimeMs,
                { editingTeammates = true }
            )
        }
    }
    if (displayScheme.showTeammates) {
        Teammates(
            editing = editingTeammates,
            savedTeammatesRad = state().teammates,
            setSavedTeammatesRad = { state { AppState.teammates set it } },
            onDone = { editingTeammates = false }
        )
    }
    ActionMenu(
        onAction = { action ->
            when (action) {
                is Action.PauseResume -> pause = !pause
                is Action.Teammates -> editingTeammates = true
                Action.Settings -> navigator.toggleSettings()
                Action.Exit -> navigator.navigate(Screen.Home)
                Action.Rotate ->
                    state { AppState.settings.general.displayRotationVertical transform { !it } }
                Action.Scale -> editingScale = true
            }
        },
        canPause =
            displayScheme.getPossibleContentPairs(state()).size > 1 ||
                state().settings.general.contentRotation.possibleRotations.size > 1,
        pause = pause,
        showTeammates = displayScheme.showTeammates,
        editingTeammates = editingTeammates
    )
    AnimatedVisibility(
        visible = editingScale,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = Modifier.fillMaxSize()
    ) {
        Scale(state, displayScheme, { editingScale = false })
    }
}

@Composable expect fun KeepScreenOn(state: AppState.Prop)

@Composable expect fun LockScreenOrientation(state: AppState.Prop)

@Composable expect fun FullScreen(state: AppState.Prop)

@Composable
private fun DisplayLabel(
    content: DisplayContent,
    vertical: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedContent(content.name, modifier = modifier) { label ->
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier.then(if (vertical) Modifier.fillMaxWidth() else Modifier.fillMaxHeight())
                    .padding(8.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                modifier = if (vertical) Modifier else Modifier.rotate90(negative = true)
            )
        }
    }
}

@Composable
private fun DisplayVertical(
    content: DisplayContentWithRotation,
    displayScheme: DisplayScheme,
    navigator: Navigator,
    state: AppState.Prop,
    windowSize: WindowSize,
    currentTimeMs: Long,
    onEditTeammates: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f).fillMaxWidth()) {
            DisplayContent(
                content,
                top = true,
                displayRotationVertical = true,
                state,
                displayScheme,
                navigator
            )
            DisplayLabel(
                content = content.displayContentPair.topContent,
                vertical = true,
                modifier =
                    Modifier.background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                1f to MaterialTheme.colorScheme.surface
                            )
                        )
                        .align(Alignment.BottomCenter)
                        .rotate(180f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(8.dp)
        ) {
            if (state().settings.general.showClock) {
                Clock(
                    currentTimeMs,
                    orientation = state().settings.general.clockOrientation,
                    setOrientation = {
                        state { AppState.settings.general.clockOrientation set it }
                    },
                    displayVertical = true,
                    leftSide = true
                )
            }
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.weight(1f)) {
                if (windowSize != WindowSize.Small && displayScheme.showTeammates) {
                    IconButtonWithEmphasis(onClick = onEditTeammates) {
                        Icon(iconRes(Res.drawable.ic_group), null)
                    }
                }
            }
            Spacer(modifier = Modifier.size(ActionMenuButtonSize))
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(1f)) {
                if (windowSize != WindowSize.Small) {
                    IconButtonWithEmphasis(onClick = { navigator.navigate(Screen.Home) }) {
                        Icon(iconRes(Res.drawable.ic_close), null)
                    }
                }
            }
            if (state().settings.general.showClock) {
                Clock(
                    currentTimeMs,
                    orientation = state().settings.general.clockOrientation,
                    setOrientation = {
                        state { AppState.settings.general.clockOrientation set it }
                    },
                    displayVertical = true,
                    leftSide = false
                )
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f).fillMaxWidth()) {
            DisplayContent(
                content,
                top = false,
                displayRotationVertical = true,
                state,
                displayScheme,
                navigator
            )
            DisplayLabel(
                content = content.displayContentPair.bottomContent,
                vertical = true,
                modifier =
                    Modifier.background(
                            Brush.verticalGradient(
                                0f to MaterialTheme.colorScheme.surface,
                                1f to Color.Transparent
                            )
                        )
                        .align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun DisplayHorizontal(
    content: DisplayContentWithRotation,
    displayScheme: DisplayScheme,
    navigator: Navigator,
    state: AppState.Prop,
    currentTimeMs: Long,
    onEditTeammates: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f).fillMaxHeight()) {
            DisplayContent(
                content,
                top = true,
                displayRotationVertical = false,
                state,
                displayScheme,
                navigator
            )
            DisplayLabel(
                content = content.displayContentPair.topContent,
                vertical = false,
                modifier =
                    Modifier.background(
                            Brush.horizontalGradient(
                                0f to Color.Transparent,
                                1f to MaterialTheme.colorScheme.surface
                            )
                        )
                        .align(Alignment.CenterEnd)
                        .rotate(180f)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier.fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(8.dp)
        ) {
            if (state().settings.general.showClock) {
                Clock(
                    currentTimeMs,
                    orientation = state().settings.general.clockOrientation,
                    setOrientation = {
                        state { AppState.settings.general.clockOrientation set it }
                    },
                    displayVertical = false,
                    leftSide = true
                )
            }
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.weight(1f)) {
                if (displayScheme.showTeammates) {
                    IconButtonWithEmphasis(onClick = onEditTeammates) {
                        Icon(iconRes(Res.drawable.ic_group), null)
                    }
                }
            }
            Spacer(modifier = Modifier.size(ActionMenuButtonSize))
            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.weight(1f)) {
                IconButtonWithEmphasis(onClick = { navigator.navigate(Screen.Home) }) {
                    Icon(iconRes(Res.drawable.ic_close), null)
                }
            }
            if (state().settings.general.showClock) {
                Clock(
                    currentTimeMs,
                    orientation = state().settings.general.clockOrientation,
                    setOrientation = {
                        state { AppState.settings.general.clockOrientation set it }
                    },
                    displayVertical = false,
                    leftSide = false
                )
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f).fillMaxHeight()) {
            DisplayContent(
                content,
                top = false,
                displayRotationVertical = false,
                state,
                displayScheme,
                navigator
            )
            DisplayLabel(
                content = content.displayContentPair.bottomContent,
                vertical = false,
                modifier =
                    Modifier.background(
                            Brush.horizontalGradient(
                                0f to MaterialTheme.colorScheme.surface,
                                1f to Color.Transparent
                            )
                        )
                        .align(Alignment.CenterStart)
            )
        }
    }
}

@Composable
private fun Clock(
    currentTimeMs: Long,
    orientation: Boolean,
    setOrientation: (Boolean) -> Unit,
    leftSide: Boolean,
    displayVertical: Boolean,
    modifier: Modifier = Modifier
) {
    val timeFormat =
        LocalDateTime.Format {
            amPmHour(Padding.NONE)
            char(':')
            minute()
        }
    val clockText =
        Instant.fromEpochMilliseconds(currentTimeMs)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .format(timeFormat)
    AnimatedContent(
        targetState = orientation,
        label = "",
        transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut() },
        modifier =
            modifier
                .clip(MaterialTheme.shapes.small)
                .clickable { setOrientation(!orientation) }
                .pointerHoverIcon(PointerIcon.Hand)
                .padding(8.dp)
    ) { targetOrientation ->
        Text(
            clockText,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                ),
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier =
                Modifier.rotate((if (targetOrientation xor leftSide) 0f else 180f))
                    .then(if (displayVertical) Modifier else Modifier.rotate90())
        )
    }
}

@Composable
private fun DisplayContent(
    content: DisplayContentWithRotation,
    top: Boolean,
    displayRotationVertical: Boolean,
    state: AppState.Prop,
    displayScheme: DisplayScheme,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        (if (top) content.displayContentPair.topContent
        else content.displayContentPair.bottomContent) to content.rotation,
        transitionSpec = {
            (fadeIn(tween(1000)) togetherWith fadeOut(tween(1000))) using
                SizeTransform(clip = false)
        },
        modifier = modifier.fillMaxSize().clipToBounds()
    ) { (displayContent, contentRotation) ->
        val slide by
            transition.animateFloat(transitionSpec = { tween(1000) }) { s ->
                when (s) {
                    EnterExitState.PreEnter -> 1f
                    EnterExitState.Visible -> 0f
                    EnterExitState.PostExit -> 1f
                }
            }
        val displayScale =
            when (displayScheme) {
                DisplayScheme.Main -> state().settings.mainDisplay.scale
                DisplayScheme.PossibleTrumps -> state().settings.possibleTrumpsDisplay.scale
            }
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Layout(
                content = {
                    when (displayContent) {
                        is DisplayContent.Trump -> TrumpDisplay(state, navigator, displayScale)
                        is DisplayContent.Calls -> CallsDisplay(state, navigator, displayScale)
                        is DisplayContent.PossibleTrumps ->
                            PossibleTrumpsDisplay(state, navigator, displayScale)
                        DisplayContent.None -> Box(Modifier)
                    }
                },
                modifier =
                    Modifier.rotate(
                            when (contentRotation) {
                                ContentRotation.Center -> if (top) 180f else 0f
                                ContentRotation.TopTowardsRight -> if (top) -90f else 90f
                                ContentRotation.BottomTowardsRight -> if (top) 90f else -90f
                            } + if (displayRotationVertical) 0f else -90f
                        )
                        .offset(
                            y =
                                when (contentRotation) {
                                    ContentRotation.Center -> maxHeight
                                    ContentRotation.TopTowardsRight,
                                    ContentRotation.BottomTowardsRight -> maxWidth
                                } * slide / 3
                        )
            ) { measurables, constraints ->
                val placeable = measurables.first().measure(Constraints())
                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.place(
                        (constraints.maxWidth - placeable.width) / 2,
                        (constraints.maxHeight - placeable.height) / 2
                    )
                }
            }
        }
    }
}
