package dialogs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.ButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import components.RankPicker
import components.SuitPicker
import model.AppState
import model.PlayingCard
import model.trump
import navigation.Navigator
import resources.Res
import resources.ic_close
import resources.ic_done
import util.iconRes

@Composable
fun EditTrumpDialog(navigator: Navigator, state: AppState.Prop) {
    var rank by rememberSaveable { mutableStateOf(state().trump?.rank) }
    var suit by rememberSaveable { mutableStateOf(state().trump?.suit) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier =
            Modifier.width(IntrinsicSize.Max)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                "Edit trump card",
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Suit",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                SuitPicker(
                    suit,
                    { suit = it },
                    state,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            modifier =
                Modifier.fillMaxWidth()
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
                onClick = {
                    rank?.let { r ->
                        suit?.let { s ->
                            state { AppState.trump set PlayingCard(r, s) }
                            navigator.closeDialog()
                        }
                    }
                },
                enabled = rank != null && suit != null
            )
        }
    }
}
