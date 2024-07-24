package dialogs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import components.PossibleTrumpsPicker
import model.AppState
import model.possibleTrumps
import navigation.Navigator
import resources.Res
import resources.ic_close
import resources.ic_done
import util.ExpandWidths
import util.iconRes

@Composable
fun EditPossibleTrumpsDialog(navigator: Navigator, state: AppState.Prop) {
    var selected by rememberSaveable { mutableStateOf(state().possibleTrumps) }
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
                    "Edit possible trumps",
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(contentAlignment = Alignment.Center, modifier = Modifier.expandWidth()) {
                    PossibleTrumpsPicker(selected, { selected = it })
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
                    icon = iconRes(Res.drawable.ic_close),
                    text = "Cancel",
                    onClick = navigator::closeDialog
                )
                ButtonWithEmphasis(
                    text = "Done",
                    icon = iconRes(Res.drawable.ic_done),
                    onClick = {
                        state { AppState.possibleTrumps set selected }
                        navigator.closeDialog()
                    }
                )
            }
        }
    }
}
