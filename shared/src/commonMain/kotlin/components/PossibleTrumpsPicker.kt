package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import interaction.PressableWithEmphasis
import resources.Res
import resources.ic_clear_all
import resources.ic_undo
import util.ExpandWidths
import util.allRanks
import util.iconRes

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
    ExpandWidths(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.expandWidth()
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
            Box(modifier = Modifier.expandWidth()) {
                OutlinedButtonWithEmphasis(
                    text = if (selected.isNotEmpty() || saved == null) "Clear" else "Undo",
                    icon =
                        iconRes(
                            if (selected.isNotEmpty() || saved == null) Res.drawable.ic_clear_all
                            else Res.drawable.ic_undo
                        ),
                    onClick = {
                        if (saved != null) {
                            saved?.let {
                                setSelected(it.sortedBy { r -> allRanks.indexOf(r) }.toSet())
                            }
                        } else {
                            saved = selected
                            setSelected(emptySet())
                        }
                    },
                    enabled = (selected.isEmpty() && saved != null) || selected.isNotEmpty(),
                    modifier = Modifier.align(Alignment.Center)
                )
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
            interactionSource = interactionSource,
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            Text(rank, modifier = Modifier.pressEmphasis())
        }
    }
}
