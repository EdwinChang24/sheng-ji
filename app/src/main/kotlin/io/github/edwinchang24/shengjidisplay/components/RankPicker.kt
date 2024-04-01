package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Composable
fun RankPicker(rank: String?, setRank: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Row {
            (2..6)
                .map { it.toString() }
                .forEach {
                    IconButton(
                        onClick = { setRank(it) },
                        colors =
                            if (rank == it) IconButtonDefaults.filledIconButtonColors()
                            else IconButtonDefaults.iconButtonColors()
                    ) {
                        Text(it)
                    }
                }
        }
        Row {
            ((7..10).map { it.toString() } + "J").forEach {
                IconButton(
                    onClick = { setRank(it) },
                    colors =
                        if (rank == it) IconButtonDefaults.filledIconButtonColors()
                        else IconButtonDefaults.iconButtonColors()
                ) {
                    Text(it)
                }
            }
        }
        Row {
            listOf("Q", "K", "A").forEach {
                IconButton(
                    onClick = { setRank(it) },
                    colors =
                        if (rank == it) IconButtonDefaults.filledIconButtonColors()
                        else IconButtonDefaults.iconButtonColors()
                ) {
                    Text(it)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RankPickerPreview() {
    ShengJiDisplayTheme {
        Surface {
            var rank: String? by rememberSaveable { mutableStateOf(null) }
            RankPicker(rank, { rank = it })
        }
    }
}
