package io.github.edwinchang24.shengjidisplay.pages

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Constraints.Companion.Infinity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.appDestination
import io.github.edwinchang24.shengjidisplay.components.CallsDisplay
import io.github.edwinchang24.shengjidisplay.components.IconButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.PlayingCard
import io.github.edwinchang24.shengjidisplay.components.PossibleTrumps
import io.github.edwinchang24.shengjidisplay.components.Teammates
import io.github.edwinchang24.shengjidisplay.destinations.EditCallDialogDestination
import io.github.edwinchang24.shengjidisplay.destinations.EditPossibleTrumpsDialogDestination
import io.github.edwinchang24.shengjidisplay.destinations.EditTrumpDialogDestination
import io.github.edwinchang24.shengjidisplay.destinations.HomePageDestination
import io.github.edwinchang24.shengjidisplay.destinations.SettingsPageDestination
import io.github.edwinchang24.shengjidisplay.display.ContentRotation
import io.github.edwinchang24.shengjidisplay.display.DisplayContent
import io.github.edwinchang24.shengjidisplay.display.DisplayContentWithRotation
import io.github.edwinchang24.shengjidisplay.display.DisplayScheme
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.AppState
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@SuppressLint("SourceLockedOrientationActivity")
@Destination(style = DisplayPageTransitions::class)
@MainNavGraph
@Composable
fun DisplayPage(
    displayScheme: DisplayScheme,
    editTeammates: Boolean = false,
    navigator: DestinationsNavigator,
    mainActivityViewModel: MainActivityViewModel,
    displayViewModel: DisplayViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by
        mainActivityViewModel.state.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    val content by
        displayViewModel.currentContent.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    var currentTimeMs by rememberSaveable {
        mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
    }
    var editingTeammates by rememberSaveable { mutableStateOf(editTeammates) }
    var pause by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(
        displayScheme.getPossibleContentPairs(state),
        state.settings.contentRotation.possibleRotations,
        pause,
        state.settings.autoSwitchSeconds
    ) {
        displayViewModel.onStateUpdate(
            newPossibleContentPairs = displayScheme.getPossibleContentPairs(state),
            newPossibleRotations = state.settings.contentRotation.possibleRotations,
            newPause = pause,
            newAutoSwitchSeconds = state.settings.autoSwitchSeconds
        )
    }
    LaunchedEffect(true) {
        currentTimeMs = Clock.System.now().toEpochMilliseconds()
        while (true) {
            delay(60_000 - (currentTimeMs % 60_000))
            currentTimeMs = Clock.System.now().toEpochMilliseconds()
        }
    }
    fun Context.activity(): Activity? =
        this as? Activity ?: (this as? ContextWrapper)?.baseContext?.activity()
    DisposableEffect(state.settings.keepScreenOn) {
        context.activity()?.window.let { window ->
            if (state.settings.keepScreenOn)
                window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            else window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
        }
    }
    DisposableEffect(state.settings.lockScreenOrientation) {
        context.activity().let { activity ->
            activity?.requestedOrientation =
                if (state.settings.lockScreenOrientation) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            onDispose {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
    val view = LocalView.current
    DisposableEffect(state.settings.fullScreen) {
        context.activity()?.window?.let { window ->
            WindowCompat.getInsetsController(window, view).run {
                if (state.settings.fullScreen) {
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
    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                DisplayContent(
                    content = content,
                    top = true,
                    state = state,
                    onEditTrump = { navigator.navigate(EditTrumpDialogDestination) },
                    onEditCallFound = { index, found ->
                        mainActivityViewModel.state.value =
                            state.copy(
                                calls =
                                    state.calls.toMutableList().also {
                                        it[index] = it[index].copy(found = found)
                                    }
                            )
                    },
                    onEditPossibleTrumps = {
                        navigator.navigate(EditPossibleTrumpsDialogDestination)
                    },
                    onNewCall = { navigator.navigate(EditCallDialogDestination(0)) }
                )
            }
            DisplayLabel(
                content = content.displayContentPair.topContent,
                modifier = Modifier.rotate(180f)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(8.dp)
            ) {
                if (state.settings.showClock) {
                    Clock(
                        currentTimeMs,
                        orientation = state.settings.clockOrientation,
                        setOrientation = {
                            mainActivityViewModel.state.value =
                                state.copy(settings = state.settings.copy(clockOrientation = it))
                        },
                        leftSide = true
                    )
                }
                ActionButtons(
                    showPause =
                        displayScheme.getPossibleContentPairs(state).size > 1 ||
                            state.settings.contentRotation.possibleRotations.size > 1,
                    pause = pause,
                    setPause = { pause = it },
                    onEditTeammates = { editingTeammates = true },
                    onNavigateSettings = { navigator.navigate(SettingsPageDestination) },
                    onExit = navigator::navigateUp,
                    modifier = Modifier.weight(1f)
                )
                if (state.settings.showClock) {
                    Clock(
                        currentTimeMs,
                        orientation = state.settings.clockOrientation,
                        setOrientation = {
                            mainActivityViewModel.state.value =
                                state.copy(settings = state.settings.copy(clockOrientation = it))
                        },
                        leftSide = false
                    )
                }
            }
            DisplayLabel(content = content.displayContentPair.bottomContent)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                DisplayContent(
                    content = content,
                    top = false,
                    state = state,
                    onEditTrump = { navigator.navigate(EditTrumpDialogDestination) },
                    onEditCallFound = { index, found ->
                        mainActivityViewModel.state.value =
                            state.copy(
                                calls =
                                    state.calls.toMutableList().also {
                                        it[index] = it[index].copy(found = found)
                                    }
                            )
                    },
                    onEditPossibleTrumps = {
                        navigator.navigate(EditPossibleTrumpsDialogDestination)
                    },
                    onNewCall = { navigator.navigate(EditCallDialogDestination(0)) }
                )
            }
        }
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .alpha(animateFloatAsState(if (editingTeammates) 0.75f else 0f).value)
                    .background(MaterialTheme.colorScheme.surface)
                    .then(if (editingTeammates) Modifier.pointerInput(true) {} else Modifier)
        )
        Teammates(
            editing = editingTeammates,
            savedTeammatesRad = state.teammates,
            setSavedTeammatesRad = {
                mainActivityViewModel.state.value = state.copy(teammates = it)
            },
            onDone = { editingTeammates = false },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun DisplayLabel(
    content: io.github.edwinchang24.shengjidisplay.display.DisplayContent,
    modifier: Modifier = Modifier
) {
    AnimatedContent(content.name, label = "") { label ->
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxWidth().padding(8.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun Clock(
    currentTimeMs: Long,
    orientation: Boolean,
    setOrientation: (Boolean) -> Unit,
    leftSide: Boolean,
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
                .padding(8.dp)
    ) { targetOrientation ->
        Text(
            clockText,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                ),
            modifier = Modifier.rotate(if (targetOrientation xor leftSide) 0f else 180f)
        )
    }
}

@Composable
private fun ActionButtons(
    showPause: Boolean,
    pause: Boolean,
    setPause: (Boolean) -> Unit,
    onEditTeammates: () -> Unit,
    onNavigateSettings: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        content = {
            if (showPause) {
                IconButtonWithEmphasis(onClick = { setPause(!pause) }) {
                    if (pause) Icon(painterResource(R.drawable.ic_play_arrow), null)
                    else Icon(painterResource(R.drawable.ic_pause), null)
                }
            }
            IconButtonWithEmphasis(onClick = onEditTeammates) {
                Icon(painterResource(R.drawable.ic_group), null)
            }
            IconButtonWithEmphasis(onClick = onNavigateSettings) {
                Icon(painterResource(R.drawable.ic_settings), null)
            }
            IconButtonWithEmphasis(onClick = onExit) {
                Icon(painterResource(R.drawable.ic_close), null)
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val newMaxWidth =
            constraints.maxWidth
                .takeIf { it != Infinity }
                ?.let { maxWidth ->
                    measurables.size.takeIf { it != 0 }?.let { btnCount -> maxWidth / btnCount }
                } ?: Infinity
        val placeables =
            measurables.map {
                it.measure(Constraints(maxWidth = newMaxWidth, maxHeight = newMaxWidth))
            }
        layout(placeables.sumOf { it.width }, placeables.maxOf { it.height }) {
            var x = 0
            placeables.forEach {
                it.placeRelative(x, 0)
                x += it.width
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DisplayContent(
    content: DisplayContentWithRotation,
    top: Boolean,
    state: AppState,
    onEditTrump: () -> Unit,
    onEditCallFound: (index: Int, found: Int) -> Unit,
    onNewCall: () -> Unit,
    onEditPossibleTrumps: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        (if (top) content.displayContentPair.topContent
        else content.displayContentPair.bottomContent) to content.rotation,
        transitionSpec = {
            (fadeIn(tween(durationMillis = 1000)) +
                    slideIntoContainer(
                        towards =
                            when (targetState.second) {
                                ContentRotation.Center ->
                                    if (top) SlideDirection.Down else SlideDirection.Up
                                ContentRotation.TopTowardsRight ->
                                    if (top) SlideDirection.Left else SlideDirection.Right
                                ContentRotation.BottomTowardsRight ->
                                    if (top) SlideDirection.Right else SlideDirection.Left
                            },
                        animationSpec = tween(durationMillis = 1000),
                        initialOffset = { it / 3 }
                    ) togetherWith
                    fadeOut(tween(durationMillis = 1000)) +
                        slideOutOfContainer(
                            towards =
                                when (initialState.second) {
                                    ContentRotation.Center ->
                                        if (top) SlideDirection.Up else SlideDirection.Down
                                    ContentRotation.TopTowardsRight ->
                                        if (top) SlideDirection.Right else SlideDirection.Left
                                    ContentRotation.BottomTowardsRight ->
                                        if (top) SlideDirection.Left else SlideDirection.Right
                                },
                            animationSpec = tween(durationMillis = 1000),
                            targetOffset = { it / 3 }
                        ))
                .using(SizeTransform(clip = false))
        },
        label = "",
        modifier = modifier.fillMaxSize()
    ) { c ->
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier.fillMaxSize()
                    .rotate(
                        when (c.second) {
                            ContentRotation.Center -> if (top) 180f else 0f
                            ContentRotation.TopTowardsRight -> if (top) -90f else 90f
                            ContentRotation.BottomTowardsRight -> if (top) 90f else -90f
                        }
                    )
        ) {
            when (c.first) {
                is DisplayContent.Trump ->
                    AnimatedContent(targetState = state.trump, label = "") { targetTrump ->
                        targetTrump?.let {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                PressableWithEmphasis {
                                    PlayingCard(
                                        card = it,
                                        textStyle = LocalTextStyle.current.copy(fontSize = 112.sp),
                                        modifier =
                                            Modifier.clip(MaterialTheme.shapes.large)
                                                .clickableForEmphasis(onClick = onEditTrump)
                                                .padding(24.dp)
                                                .pressEmphasis()
                                    )
                                }
                            }
                        }
                            ?: Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                PressableWithEmphasis {
                                    Column(
                                        verticalArrangement =
                                            Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier =
                                            Modifier.clip(MaterialTheme.shapes.large)
                                                .clickableForEmphasis(onClick = onEditTrump)
                                                .padding(24.dp)
                                                .pressEmphasis()
                                    ) {
                                        Text("No trump card selected")
                                        OutlinedButtonWithEmphasis(onClick = onEditTrump) {
                                            Icon(painterResource(R.drawable.ic_add), null)
                                            Text("Add")
                                        }
                                    }
                                }
                            }
                    }
                is DisplayContent.Calls ->
                    AnimatedContent(
                        targetState = state.calls,
                        contentKey = { it.isEmpty() },
                        label = ""
                    ) { targetCalls ->
                        targetCalls
                            .takeIf { it.isNotEmpty() }
                            ?.let {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    CallsDisplay(calls = state.calls, setFound = onEditCallFound)
                                }
                            }
                            ?: Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                PressableWithEmphasis {
                                    Column(
                                        verticalArrangement =
                                            Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier =
                                            Modifier.clip(MaterialTheme.shapes.large)
                                                .clickableForEmphasis(onClick = onNewCall)
                                                .padding(24.dp)
                                                .pressEmphasis()
                                    ) {
                                        Text("No calls added")
                                        OutlinedButton(onClick = onNewCall) {
                                            Icon(painterResource(R.drawable.ic_add), null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Add")
                                        }
                                    }
                                }
                            }
                    }
                is DisplayContent.PossibleTrumps -> {
                    val scale = remember { Animatable(0.5f) }
                    LaunchedEffect(transition.targetState) {
                        if (transition.targetState == EnterExitState.PostExit)
                            scale.animateTo(0.5f, tween(1000))
                        else scale.animateTo(1f, tween(1000))
                    }
                    PressableWithEmphasis {
                        if (state.possibleTrumps.isEmpty()) {
                            Column(
                                verticalArrangement =
                                    Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier =
                                    Modifier.clip(MaterialTheme.shapes.large)
                                        .clickableForEmphasis(onClick = onEditPossibleTrumps)
                                        .padding(24.dp)
                                        .pressEmphasis()
                            ) {
                                Text("No possible trumps added")
                                OutlinedButton(onClick = onEditPossibleTrumps) {
                                    Icon(painterResource(R.drawable.ic_add), null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add")
                                }
                            }
                        } else {
                            PossibleTrumps(
                                state.possibleTrumps,
                                modifier =
                                    Modifier.fillMaxSize(scale.value)
                                        .clickableForEmphasis(onClick = onEditPossibleTrumps)
                                        .pressEmphasis()
                            )
                        }
                    }
                }
                DisplayContent.None -> Box(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

object DisplayPageTransitions : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() =
        when (initialState.appDestination()) {
            HomePageDestination -> fadeIn(tween(delayMillis = 250))
            else -> fadeIn()
        }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() =
        when (targetState.appDestination()) {
            HomePageDestination -> fadeOut(tween(durationMillis = 250))
            else -> fadeOut()
        }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition() =
        when (initialState.appDestination()) {
            HomePageDestination -> fadeIn(tween(delayMillis = 250))
            else -> fadeIn()
        }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() =
        when (targetState.appDestination()) {
            HomePageDestination -> fadeOut(tween(durationMillis = 250))
            else -> fadeOut()
        }
}
