package io.github.edwinchang24.shengjidisplay

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
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

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun App(state: AppState, setState: (AppState) -> Unit, modifier: Modifier = Modifier) {
    ShengJiDisplayTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets(0))
        ) {
            val windowSizeClass = calculateWindowSizeClass()
            var currentScreen by
                rememberSaveable(stateSaver = Screen.Saver) { mutableStateOf(Screen.Home) }
            var settingsOpen by rememberSaveable { mutableStateOf(false) }
            var currentDialog by
                rememberSaveable(stateSaver = Dialog.Saver) { mutableStateOf(null) }
            val navigator =
                object : Navigator {
                    override fun navigate(screen: Screen) {
                        currentScreen = screen
                    }

                    override fun toggleSettings() {
                        settingsOpen = !settingsOpen
                    }

                    override fun navigate(dialog: Dialog) {
                        currentDialog = dialog
                    }

                    override fun closeDialog() {
                        currentDialog = null
                    }
                }
            AndroidBackHandler(currentScreen, settingsOpen, currentDialog, navigator)
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(tween(250, delayMillis = 250)) togetherWith fadeOut(tween(250))
                },
                modifier = Modifier.fillMaxSize()
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.Home -> HomePage(navigator, state, setState)
                    is Screen.Display ->
                        DisplayPage(
                            targetScreen.scheme,
                            targetScreen.editTeammates,
                            navigator,
                            state,
                            setState
                        )
                }
            }
            AnimatedVisibility(
                visible = settingsOpen,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ) {
                Box(
                    modifier =
                        Modifier.fillMaxSize()
                            .animateEnterExit(enter = fadeIn(), exit = fadeOut())
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                            .pointerInput(true) {
                                detectTapGestures { settingsOpen = false }
                                detectDragGestures(
                                    onDragEnd = { settingsOpen = false },
                                    onDragCancel = { settingsOpen = false },
                                    onDrag = { _, _ -> }
                                )
                            }
                )
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier =
                        if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact)
                            Modifier.fillMaxSize()
                        else Modifier.fillMaxHeight()
                ) {
                    SettingsPage(navigator, state, setState)
                }
            }
            AnimatedContent(
                targetState = currentDialog,
                transitionSpec = {
                    (fadeIn(tween(200)) + slideInVertically { it / 16 } togetherWith
                            fadeOut(tween(200)) + slideOutVertically { -it / 8 })
                        .using(sizeTransform = null)
                },
                modifier = Modifier.fillMaxSize()
            ) { targetDialog ->
                if (targetDialog != null) {
                    Box(
                        modifier =
                            Modifier.fillMaxSize()
                                .animateEnterExit(enter = fadeIn(), exit = fadeOut())
                                .alpha(0.75f)
                                .background(MaterialTheme.colorScheme.background)
                                .pointerInput(true) {
                                    detectTapGestures { currentDialog = null }
                                    detectDragGestures(
                                        onDragEnd = { currentDialog = null },
                                        onDragCancel = { currentDialog = null },
                                        onDrag = { _, _ -> }
                                    )
                                }
                    )
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Card(
                            shape = MaterialTheme.shapes.large,
                            colors =
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier =
                                    Modifier.width(IntrinsicSize.Max)
                                        .verticalScroll(rememberScrollState())
                                        .padding(24.dp)
                            ) {
                                Text(
                                    targetDialog.title,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                when (targetDialog) {
                                    is Dialog.EditCall ->
                                        EditCallDialog(
                                            targetDialog.index,
                                            navigator,
                                            state,
                                            setState
                                        )
                                    Dialog.EditPossibleTrumps ->
                                        EditPossibleTrumpsDialog(navigator, state, setState)
                                    Dialog.EditTrump -> EditTrumpDialog(navigator, state, setState)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
