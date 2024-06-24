package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_clear_all
import io.github.edwinchang24.shengjidisplay.resources.ic_undo
import io.github.edwinchang24.shengjidisplay.util.allRanks
import org.jetbrains.compose.resources.painterResource

@Composable
fun PossibleTrumpsPicker(
    selected: Set<String>,
    setSelected: (Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var saved: Set<String>? by rememberSaveable { mutableStateOf(null) }
    LaunchedEffect(selected) { if (selected.isNotEmpty()) saved = null }
    val toggleRank = { rank: String ->
        setSelected(
            selected
                .toMutableSet()
                .apply { if (rank in this) remove(rank) else add(rank) }
                .sortedBy { allRanks.indexOf(it) }
                .toSet()
        )
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                (2..6).map { it.toString() }.forEach { RankButton(it, toggleRank, it in selected) }
            }
            Row {
                ((7..10).map { it.toString() } + "J").forEach {
                    RankButton(it, toggleRank, it in selected)
                }
            }
            Row { listOf("Q", "K", "A").forEach { RankButton(it, toggleRank, it in selected) } }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButtonWithEmphasis(
                text = if (selected.isNotEmpty() || saved == null) "Clear" else "Undo",
                icon =
                    painterResource(
                        if (selected.isNotEmpty() || saved == null) Res.drawable.ic_clear_all
                        else Res.drawable.ic_undo
                    ),
                onClick = {
                    if (saved != null) {
                        saved?.let { setSelected(it.sortedBy { allRanks.indexOf(it) }.toSet()) }
                    } else {
                        saved = selected
                        setSelected(emptySet())
                    }
                },
                enabled = (selected.isEmpty() && saved != null) || selected.isNotEmpty(),
                modifier = Modifier.align(Alignment.Center),
            )
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
