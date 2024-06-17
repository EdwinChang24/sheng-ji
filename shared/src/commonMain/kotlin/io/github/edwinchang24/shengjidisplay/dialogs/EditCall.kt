package io.github.edwinchang24.shengjidisplay.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.NumberPicker
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.RankPicker
import io.github.edwinchang24.shengjidisplay.components.SuitPicker
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.model.PlayingCard
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_close
import io.github.edwinchang24.shengjidisplay.resources.ic_done
import org.jetbrains.compose.resources.painterResource

/** @param index index of call to edit; will create a new call if index is out of bounds */
@Composable
fun EditCallDialog(
    index: Int,
    navigator: Navigator,
    state: AppState,
    setState: (AppState) -> Unit
) {
    var rank by rememberSaveable { mutableStateOf(state.calls.getOrNull(index)?.card?.rank) }
    var suit by rememberSaveable { mutableStateOf(state.calls.getOrNull(index)?.card?.suit) }
    var number by rememberSaveable { mutableIntStateOf(state.calls.getOrNull(index)?.number ?: 1) }
    fun onDone() {
        rank?.let { r ->
            suit?.let { s ->
                val calls =
                    state.calls.toMutableList().apply {
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
                setState(state.copy(calls = calls))
                navigator.closeDialog()
            }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text("Rank", style = MaterialTheme.typography.labelMedium)
        RankPicker(rank, { rank = it }, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text("Suit", style = MaterialTheme.typography.labelMedium)
        SuitPicker(suit, { suit = it }, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text("Number", style = MaterialTheme.typography.labelMedium)
        NumberPicker(number, { number = it })
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButtonWithEmphasis(onClick = navigator::closeDialog) {
            Icon(painterResource(Res.drawable.ic_close), null)
            Text("Cancel")
        }
        ButtonWithEmphasis(onClick = { onDone() }, enabled = rank != null && suit != null) {
            Icon(painterResource(Res.drawable.ic_done), null)
            Text("Done")
        }
    }
}
