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
import model.PlayingCard
import model.trump
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_info
import resources.ic_settings
import util.WindowSize
import util.calculateWindowSize
import util.iconRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navigator: Navigator, state: AppState.Prop) {
    val windowSize = calculateWindowSize()
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
            val large = windowSize == WindowSize.Large
            var startButtonsHeight by rememberSaveable { mutableStateOf(0) }
            val startButtonsHeightDp = with(LocalDensity.current) { startButtonsHeight.toDp() }
            var tempTrumpRank by
                rememberSaveable(state().trump) { mutableStateOf(state().trump?.rank) }
            var tempTrumpSuit by
                rememberSaveable(state().trump) { mutableStateOf(state().trump?.suit) }
            LaunchedEffect(tempTrumpRank, tempTrumpSuit) {
                tempTrumpRank?.let { r ->
                    tempTrumpSuit?.let { s -> state { AppState.trump set PlayingCard(r, s) } }
                }
            }
            val cardColors =
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            if (windowSize == WindowSize.Small) {
                Column(
                    modifier =
                        Modifier.verticalScroll(rememberScrollState())
                            .padding(12.dp)
                            .padding(bottom = startButtonsHeightDp)
                ) {
                    PossibleTrumpsSelection(
                        cardColors,
                        windowSize,
                        navigator,
                        state,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                    TrumpCardSelection(
                        cardColors,
                        tempTrumpRank,
                        { tempTrumpRank = it },
                        tempTrumpSuit,
                        { tempTrumpSuit = it },
                        windowSize,
                        navigator,
                        state,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                    CallsSelection(
                        cardColors,
                        navigator,
                        state,
                        modifier = Modifier.fillMaxWidth().padding(12.dp)
                    )
                    TeammatesSelection(
                        cardColors,
                        navigator,
                        state,
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
                                        (WindowSize.Medium.breakpoint +
                                            WindowSize.Large.breakpoint) / 2
                                    )
                                    .padding(12.dp)
                                    .padding(bottom = if (!large) startButtonsHeightDp else 0.dp)
                        ) {
                            Row {
                                PossibleTrumpsSelection(
                                    cardColors,
                                    windowSize,
                                    navigator,
                                    state,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                                TrumpCardSelection(
                                    cardColors,
                                    tempTrumpRank,
                                    { tempTrumpRank = it },
                                    tempTrumpSuit,
                                    { tempTrumpSuit = it },
                                    windowSize,
                                    navigator,
                                    state,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                            }
                            Row {
                                CallsSelection(
                                    cardColors,
                                    navigator,
                                    state,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                                TeammatesSelection(
                                    cardColors,
                                    navigator,
                                    state,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                            }
                        }
                    }
                    if (large) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxHeight().weight(1f).padding(24.dp)
                        ) {
                            StartButtons(navigator)
                        }
                    }
                }
            }
            if (!large) {
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
