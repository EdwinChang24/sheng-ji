package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis

@Composable
fun RankPicker(rank: String?, setRank: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Row { (2..6).map { it.toString() }.forEach { RankButton(it, setRank, rank == it) } }
        Row {
            ((7..10).map { it.toString() } + "J").forEach { RankButton(it, setRank, rank == it) }
        }
        Row { listOf("Q", "K", "A").forEach { RankButton(it, setRank, rank == it) } }
    }
}

@Composable
private fun RankButton(rank: String, setRank: (String) -> Unit, selected: Boolean) {
    PressableWithEmphasis {
        IconButton(
            onClick = { setRank(rank) },
            colors =
                if (selected) IconButtonDefaults.filledIconButtonColors()
                else IconButtonDefaults.iconButtonColors(),
            interactionSource = interactionSource
        ) {
            Text(
                rank,
                maxLines = 1,
                overflow = TextOverflow.Visible,
                modifier = Modifier.pressEmphasis()
            )
        }
    }
}
