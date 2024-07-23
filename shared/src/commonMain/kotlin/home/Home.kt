package home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
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
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
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
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                val callsLayoutId = "calls"
                val cards =
                    @Composable {
                        PossibleTrumpsSelection(
                            cardColors,
                            windowSize,
                            navigator,
                            state,
                            modifier = Modifier.padding(12.dp)
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
                            modifier = Modifier.padding(12.dp)
                        )
                        CallsSelection(
                            cardColors,
                            navigator,
                            state,
                            modifier = Modifier.padding(12.dp).layoutId(callsLayoutId)
                        )
                        TeammatesSelection(
                            cardColors,
                            navigator,
                            state,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                SubcomposeLayout(
                    modifier =
                        Modifier.verticalScroll(rememberScrollState())
                            .padding(12.dp)
                            .padding(bottom = if (!large) startButtonsHeightDp else 0.dp)
                ) { constraints ->
                    if (windowSize == WindowSize.Small) {
                        val placeables =
                            subcompose(0, cards).map {
                                it.measure(
                                    constraints.copy(
                                        minWidth = constraints.maxWidth,
                                        maxWidth = constraints.maxWidth
                                    )
                                )
                            }
                        layout(constraints.maxWidth, placeables.sumOf { it.height }) {
                            var y = 0
                            placeables.forEach {
                                it.placeRelative(0, y)
                                y += it.height
                            }
                        }
                    } else {
                        var slotId = 0
                        val placeablesFiltered =
                            subcompose(slotId++, cards)
                                .filterNot { it.layoutId == callsLayoutId }
                                .map { it.measure(constraints) }
                        val width =
                            minOf(constraints.maxWidth, placeablesFiltered.maxOf { it.width } * 2)
                        val placeablesWithFinalWidth =
                            subcompose(slotId++, cards).map {
                                it.measure(
                                    constraints.copy(minWidth = width / 2, maxWidth = width / 2)
                                )
                            }
                        val maxHeights =
                            placeablesWithFinalWidth.chunked(2).map { it.maxOf { p -> p.height } }
                        val placeablesFinal =
                            subcompose(slotId, cards).mapIndexed { index, m ->
                                m.measure(
                                    Constraints(
                                        minWidth = width / 2,
                                        maxWidth = width / 2,
                                        minHeight = maxHeights[index / 2],
                                        maxHeight = maxHeights[index / 2]
                                    )
                                )
                            }
                        layout(width, maxHeights.sum()) {
                            var y = 0
                            placeablesFinal.chunked(2).forEach { row ->
                                var x = 0
                                row.forEach {
                                    it.placeRelative(x, y)
                                    x += it.width
                                }
                                y += row.maxOf { it.height }
                            }
                        }
                    }
                }
                if (large) {
                    StartButtons(navigator, modifier = Modifier.align(Alignment.CenterVertically))
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
