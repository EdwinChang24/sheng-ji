package home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import components.AppName
import components.IconButtonWithEmphasis
import model.AppState
import model.Call
import model.PlayingCard
import model.calls
import model.possibleTrumps
import model.teammates
import model.trump
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_info
import resources.ic_settings
import util.ClearableState
import util.WindowWidth
import util.calculateWindowWidth
import util.iconRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navigator: Navigator, state: AppState.Prop) {
    val windowWidth = calculateWindowWidth()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppName() },
                actions = {
                    IconButtonWithEmphasis(onClick = { navigator.toggleSettings() }) {
                        Icon(iconRes(Res.drawable.ic_settings), null)
                    }
                    IconButtonWithEmphasis(onClick = { navigator.navigate(Dialog.About) }) {
                        Icon(iconRes(Res.drawable.ic_info), null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            var startButtonsHeight by rememberSaveable { mutableStateOf(0) }
            val startButtonsHeightDp = with(LocalDensity.current) { startButtonsHeight.toDp() }
            var recentlyClearedPossibleTrumps: Set<String>? by rememberSaveable {
                mutableStateOf(null)
            }
            LaunchedEffect(state().possibleTrumps) {
                if (state().possibleTrumps.isNotEmpty()) recentlyClearedPossibleTrumps = null
            }
            val possibleTrumpsState =
                ClearableState(
                    value = state().possibleTrumps,
                    setValue = { state { AppState.possibleTrumps set it } },
                    clearValue = {
                        recentlyClearedPossibleTrumps = state().possibleTrumps
                        state { AppState.possibleTrumps set emptySet() }
                    },
                    canUndoClear =
                        state().possibleTrumps.isEmpty() && recentlyClearedPossibleTrumps != null,
                    undoClearValue = {
                        recentlyClearedPossibleTrumps?.let {
                            state { AppState.possibleTrumps set it }
                        }
                        recentlyClearedPossibleTrumps = null
                    }
                )
            var tempTrumpRank by
                rememberSaveable(state().trump) { mutableStateOf(state().trump?.rank) }
            var tempTrumpSuit by
                rememberSaveable(state().trump) { mutableStateOf(state().trump?.suit) }
            LaunchedEffect(tempTrumpRank, tempTrumpSuit) {
                tempTrumpRank?.let { r ->
                    tempTrumpSuit?.let { s -> state { AppState.trump set PlayingCard(r, s) } }
                }
            }
            var recentlyClearedCalls: List<Call>? by rememberSaveable { mutableStateOf(null) }
            val callsState =
                ClearableState(
                    value = state().calls,
                    setValue = { state { AppState.calls set it } },
                    clearValue = {
                        recentlyClearedCalls = state().calls
                        state { AppState.calls set emptyList() }
                    },
                    canUndoClear = state().calls.isEmpty() && recentlyClearedCalls != null,
                    undoClearValue = {
                        recentlyClearedCalls?.let { state { AppState.calls set it } }
                        recentlyClearedCalls = null
                    }
                )
            var recentlyClearedTeammates: Map<String, Float>? by rememberSaveable {
                mutableStateOf(null)
            }
            LaunchedEffect(state().teammates) {
                if (state().teammates.isNotEmpty()) recentlyClearedTeammates = null
            }
            val teammatesState =
                ClearableState(
                    value = state().teammates,
                    setValue = { state { AppState.teammates set it } },
                    clearValue = {
                        recentlyClearedTeammates = state().teammates
                        state { AppState.teammates set emptyMap() }
                    },
                    canUndoClear = state().teammates.isEmpty() && recentlyClearedTeammates != null,
                    undoClearValue = {
                        recentlyClearedTeammates?.let { state { AppState.teammates set it } }
                        recentlyClearedTeammates = null
                    }
                )
            val cardColors =
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            if (windowWidth <= WindowWidth.Small) {
                Column(
                    modifier =
                        Modifier.verticalScroll(rememberScrollState())
                            .padding(12.dp)
                            .padding(bottom = startButtonsHeightDp)
                ) {
                    PossibleTrumpsSelection(
                        possibleTrumpsState,
                        cardColors,
                        windowWidth,
                        navigator,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                    TrumpCardSelection(
                        cardColors,
                        tempTrumpRank,
                        { tempTrumpRank = it },
                        tempTrumpSuit,
                        { tempTrumpSuit = it },
                        windowWidth,
                        navigator,
                        state,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                    CallsSelection(
                        callsState,
                        cardColors,
                        navigator,
                        state,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                    TeammatesSelection(
                        teammatesState,
                        cardColors,
                        navigator,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                }
            } else {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.verticalScroll(rememberScrollState()).weight(2f)
                    ) {
                        Column(
                            modifier =
                                Modifier.widthIn(
                                        0.dp,
                                        (WindowWidth.Medium.breakpoint +
                                            WindowWidth.Large.breakpoint) / 2
                                    )
                                    .padding(12.dp)
                                    .padding(
                                        bottom =
                                            if (windowWidth < WindowWidth.Large) startButtonsHeightDp
                                            else 0.dp
                                    )
                        ) {
                            Row {
                                PossibleTrumpsSelection(
                                    possibleTrumpsState,
                                    cardColors,
                                    windowWidth,
                                    navigator,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                                TrumpCardSelection(
                                    cardColors,
                                    tempTrumpRank,
                                    { tempTrumpRank = it },
                                    tempTrumpSuit,
                                    { tempTrumpSuit = it },
                                    windowWidth,
                                    navigator,
                                    state,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                            }
                            Row {
                                CallsSelection(
                                    callsState,
                                    cardColors,
                                    navigator,
                                    state,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                                TeammatesSelection(
                                    teammatesState,
                                    cardColors,
                                    navigator,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                            }
                        }
                    }
                    if (windowWidth >= WindowWidth.Large) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxHeight().weight(1f).padding(24.dp)
                        ) {
                            StartButtons(navigator)
                        }
                    }
                }
            }
            if (windowWidth < WindowWidth.Large) {
                StartButtons(
                    navigator,
                    modifier =
                        Modifier.align(Alignment.BottomCenter)
                            .onGloballyPositioned { startButtonsHeight = it.size.height }
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                )
            }
        }
    }
}
