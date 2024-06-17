package io.github.edwinchang24.shengjidisplay.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_clear_all
import io.github.edwinchang24.shengjidisplay.resources.ic_close
import io.github.edwinchang24.shengjidisplay.resources.ic_done
import io.github.edwinchang24.shengjidisplay.resources.ic_undo
import io.github.edwinchang24.shengjidisplay.util.allRanks
import org.jetbrains.compose.resources.painterResource

@Composable
fun EditPossibleTrumpsDialog(navigator: Navigator, state: AppState, setState: (AppState) -> Unit) {
    var selected by rememberSaveable { mutableStateOf(state.possibleTrumps) }
    var saved: Set<String>? by rememberSaveable { mutableStateOf(null) }
    LaunchedEffect(selected) { if (selected.isNotEmpty()) saved = null }
    val toggleRank = { rank: String ->
        selected = selected.toMutableSet().apply { if (rank in this) remove(rank) else add(rank) }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Row { (2..6).map { it.toString() }.forEach { RankButton(it, toggleRank, it in selected) } }
        Row {
            ((7..10).map { it.toString() } + "J").forEach {
                RankButton(it, toggleRank, it in selected)
            }
        }
        Row { listOf("Q", "K", "A").forEach { RankButton(it, toggleRank, it in selected) } }
    }
    Box(modifier = Modifier.fillMaxWidth()) {
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
            modifier = Modifier.align(Alignment.Center),
        ) {
            if (selected.isNotEmpty() || saved == null) {
                Icon(painterResource(Res.drawable.ic_clear_all), null)
                Text("Clear")
            } else {
                Icon(painterResource(Res.drawable.ic_undo), null)
                Text("Undo")
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButtonWithEmphasis(onClick = navigator::closeDialog) {
            Icon(painterResource(Res.drawable.ic_close), null)
            Text("Cancel")
        }
        ButtonWithEmphasis(
            onClick = {
                setState(
                    state.copy(possibleTrumps = selected.sortedBy { allRanks.indexOf(it) }.toSet())
                )
                navigator.closeDialog()
            }
        ) {
            Icon(painterResource(Res.drawable.ic_done), null)
            Text("Done")
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
