package dialogs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.ButtonWithEmphasis
import components.NumberPicker
import components.OutlinedButtonWithEmphasis
import components.RankPicker
import components.SuitPicker
import model.AppState
import model.Call
import model.PlayingCard
import model.calls
import navigation.Navigator
import resources.Res
import resources.ic_close
import resources.ic_done
import util.ExpandWidths
import util.iconRes

/** @param index index of call to edit; will create a new call if index is out of bounds */
@Composable
fun EditCallDialog(index: Int, navigator: Navigator, state: AppState.Prop) {
    var rank by rememberSaveable { mutableStateOf(state().calls.getOrNull(index)?.card?.rank) }
    var suit by rememberSaveable { mutableStateOf(state().calls.getOrNull(index)?.card?.suit) }
    var number by rememberSaveable {
        mutableIntStateOf(state().calls.getOrNull(index)?.number ?: 1)
    }
    fun onDone() {
        rank?.let { r ->
            suit?.let { s ->
                val calls =
                    state().calls.toMutableList().apply {
                        if (index in indices) {
                            this[index] =
                                this[index].copy(
                                    card = PlayingCard(r, s),
                                    number = number,
                                    found =
                                        this[index].found.let { if (it > number) number else it }
                                )
                        } else {
                            add(Call(PlayingCard(r, s), number, found = 0))
                        }
                    }
                state { AppState.calls set calls }
                navigator.closeDialog()
            }
        }
    }
    ExpandWidths {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(vertical = 24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    "Edit call",
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.expandWidth()
                ) {
                    Text(
                        "Rank",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    RankPicker(
                        rank,
                        { rank = it },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.expandWidth()
                ) {
                    Text(
                        "Suit",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    SuitPicker(suit, { suit = it }, state, modifier = Modifier.expandWidth())
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.expandWidth()
                ) {
                    Text(
                        "Number",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    NumberPicker(number, { number = it }, modifier = Modifier.expandWidth())
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier =
                    Modifier.expandWidth()
                        .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                        .padding(horizontal = 24.dp)
            ) {
                OutlinedButtonWithEmphasis(
                    text = "Cancel",
                    icon = iconRes(Res.drawable.ic_close),
                    onClick = navigator::closeDialog
                )
                ButtonWithEmphasis(
                    text = "Done",
                    icon = iconRes(Res.drawable.ic_done),
                    onClick = { onDone() },
                    enabled = rank != null && suit != null
                )
            }
        }
    }
}
