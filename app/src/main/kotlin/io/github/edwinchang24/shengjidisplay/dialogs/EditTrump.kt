package io.github.edwinchang24.shengjidisplay.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.RankPicker
import io.github.edwinchang24.shengjidisplay.components.SuitPicker
import io.github.edwinchang24.shengjidisplay.model.PlayingCard

@Destination(style = DestinationStyle.Dialog::class)
@MainNavGraph
@Composable
fun EditTrumpDialog(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    var rank by rememberSaveable { mutableStateOf(viewModel.state.value.trump?.rank) }
    var suit by rememberSaveable { mutableStateOf(viewModel.state.value.trump?.suit) }
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.width(IntrinsicSize.Max).windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(24.dp).verticalScroll(rememberScrollState())
        ) {
            Text("Edit trump card", style = MaterialTheme.typography.headlineMedium)
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
                        rank?.let { r ->
                            suit?.let { s ->
                                viewModel.state.value =
                                    viewModel.state.value.copy(trump = PlayingCard(r, s))
                                navigator.navigateUp()
                            }
                        }
                    },
                    enabled = rank != null && suit != null
                ) {
                    Icon(painterResource(R.drawable.ic_done), null)
                    Text("Done")
                }
            }
        }
    }
}
