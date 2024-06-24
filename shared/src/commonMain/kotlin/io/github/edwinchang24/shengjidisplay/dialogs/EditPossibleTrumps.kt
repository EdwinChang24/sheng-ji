package io.github.edwinchang24.shengjidisplay.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.PossibleTrumpsPicker
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_close
import io.github.edwinchang24.shengjidisplay.resources.ic_done
import org.jetbrains.compose.resources.painterResource

@Composable
fun EditPossibleTrumpsDialog(navigator: Navigator, state: AppState, setState: (AppState) -> Unit) {
    var selected by rememberSaveable { mutableStateOf(state.possibleTrumps) }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        PossibleTrumpsPicker(selected, { selected = it })
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedButtonWithEmphasis(
            icon = painterResource(Res.drawable.ic_close),
            text = "Cancel",
            onClick = navigator::closeDialog
        )
        ButtonWithEmphasis(
            text = "Done",
            icon = painterResource(Res.drawable.ic_done),
            onClick = {
                setState(state.copy(possibleTrumps = selected))
                navigator.closeDialog()
            }
        )
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
