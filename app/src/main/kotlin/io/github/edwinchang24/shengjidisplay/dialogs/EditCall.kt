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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.NumberPicker
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.RankPicker
import io.github.edwinchang24.shengjidisplay.components.SuitPicker
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.model.PlayingCard

/** @param index index of call to edit; will create a new call if index is out of bounds */
@Destination(style = DestinationStyle.Dialog::class)
@MainNavGraph
@Composable
fun EditCallDialog(
    index: Int,
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultBackNavigator<Int>,
    viewModel: MainActivityViewModel
) {
    var rank by rememberSaveable {
        mutableStateOf(viewModel.state.value.calls.getOrNull(index)?.card?.rank)
    }
    var suit by rememberSaveable {
        mutableStateOf(viewModel.state.value.calls.getOrNull(index)?.card?.suit)
    }
    var number by rememberSaveable {
        mutableIntStateOf(viewModel.state.value.calls.getOrNull(index)?.number ?: 1)
    }
    fun onDone() {
        rank?.let { r ->
            suit?.let { s ->
                viewModel.state.value =
                    viewModel.state.value.copy(
                        calls =
                            viewModel.state.value.calls.toMutableList().apply {
                                if (index in indices) {
                                    this[index] =
                                        this[index].copy(
                                            card = PlayingCard(r, s),
                                            number = number,
                                            found =
                                                this[index].found.let {
                                                    if (it > number) number else it
                                                }
                                        )
                                } else {
                                    add(Call(PlayingCard(r, s), number, found = 0))
                                }
                            }
                    )
                resultBackNavigator.navigateBack(index)
            }
        }
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
            Text("Edit call", style = MaterialTheme.typography.headlineMedium)
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Rank", style = MaterialTheme.typography.labelMedium)
                RankPicker(
                    rank,
                    { rank = it },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Suit", style = MaterialTheme.typography.labelMedium)
                SuitPicker(
                    suit,
                    { suit = it },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Number", style = MaterialTheme.typography.labelMedium)
                NumberPicker(number, { number = it })
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButtonWithEmphasis(onClick = navigator::navigateUp) {
                    Icon(painterResource(R.drawable.ic_close), null)
                    Text("Cancel")
                }
                ButtonWithEmphasis(onClick = { onDone() }, enabled = rank != null && suit != null) {
                    Icon(painterResource(R.drawable.ic_done), null)
                    Text("Done")
                }
            }
        }
    }
}
