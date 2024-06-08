package io.github.edwinchang24.shengjidisplay.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.util.allRanks

@Destination(style = DestinationStyle.Dialog::class)
@MainNavGraph
@Composable
fun EditPossibleTrumpsDialog(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    val state by
        viewModel.state.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    var selected by rememberSaveable { mutableStateOf(state.possibleTrumps) }
    var saved: Set<String>? by rememberSaveable { mutableStateOf(null) }
    LaunchedEffect(selected) { if (selected.isNotEmpty()) saved = null }
    val toggleRank = { rank: String ->
        selected = selected.toMutableSet().apply { if (rank in this) remove(rank) else add(rank) }
    }
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.width(IntrinsicSize.Max).windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(24.dp).verticalScroll(rememberScrollState())
        ) {
            Text("Edit possible trumps", style = MaterialTheme.typography.headlineMedium)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    (2..6)
                        .map { it.toString() }
                        .forEach { RankButton(it, toggleRank, it in selected) }
                }
                Row {
                    ((7..10).map { it.toString() } + "J").forEach {
                        RankButton(it, toggleRank, it in selected)
                    }
                }
                Row { listOf("Q", "K", "A").forEach { RankButton(it, toggleRank, it in selected) } }
            }

            OutlinedButtonWithEmphasis(
                onClick = {
                    if (saved != null) {
                        saved?.let { selected = it }
                    } else {
                        saved = selected
                        selected = emptySet()
                    }
                },
                enabled = (selected.isEmpty() && saved != null) || selected.isNotEmpty(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                if (selected.isNotEmpty() || saved == null) {
                    Icon(painterResource(R.drawable.ic_clear_all), null)
                    Text("Clear")
                } else {
                    Icon(painterResource(R.drawable.ic_undo), null)
                    Text("Undo")
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButtonWithEmphasis(onClick = navigator::navigateUp) {
                    Icon(painterResource(R.drawable.ic_close), null)
                    Text("Cancel")
                }
                ButtonWithEmphasis(
                    onClick = {
                        viewModel.state.value =
                            state.copy(
                                possibleTrumps = selected.sortedBy { allRanks.indexOf(it) }.toSet()
                            )
                        navigator.navigateUp()
                    }
                ) {
                    Icon(painterResource(R.drawable.ic_done), null)
                    Text("Done")
                }
            }
        }
    }
}

@Composable
private fun RankButton(rank: String, toggleRank: (String) -> Unit, selected: Boolean) {
    PressableWithEmphasis {
        IconButton(
            onClick = { toggleRank(rank) },
            colors =
                if (selected) IconButtonDefaults.filledIconButtonColors()
                else IconButtonDefaults.iconButtonColors(),
            interactionSource = interactionSource
        ) {
            Text(rank, modifier = Modifier.pressEmphasis())
        }
    }
}
