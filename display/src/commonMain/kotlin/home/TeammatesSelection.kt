package home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.OutlinedButtonWithEmphasis
import display.DisplayScheme
import navigation.Navigator
import navigation.Screen
import resources.Res
import resources.ic_clear_all
import resources.ic_edit
import resources.ic_undo
import util.ClearableState
import util.DefaultTransition
import util.iconRes

@Composable
fun TeammatesSelection(
    teammatesState: ClearableState<Map<String, Float>>,
    cardColors: CardColors,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    Card(
        colors = cardColors,
        modifier =
            modifier
                .clip(CardDefaults.shape)
                .clickable {
                    navigator.navigate(
                        Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                    )
                }
                .pointerHoverIcon(PointerIcon.Hand)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Text(
                "Teammates",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            AnimatedContent(
                targetState = teammatesState.value.size,
                transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
                modifier = Modifier.padding(horizontal = 24.dp)
            ) { targetState ->
                Text(
                    "$targetState teammate${if (targetState == 1) "" else "s"} added",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
            ) {
                OutlinedButtonWithEmphasis(
                    text = "Edit",
                    icon = iconRes(Res.drawable.ic_edit),
                    onClick = {
                        navigator.navigate(
                            Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                        )
                    }
                )
                OutlinedButtonWithEmphasis(
                    text = if (teammatesState.canUndoClear) "Undo clear" else "Clear",
                    icon =
                        iconRes(
                            if (teammatesState.canUndoClear) Res.drawable.ic_undo
                            else Res.drawable.ic_clear_all
                        ),
                    onClick = {
                        if (teammatesState.canUndoClear) teammatesState.undoClearValue()
                        else teammatesState.clearValue()
                    },
                    enabled = teammatesState.canUndoClear || teammatesState.value.isNotEmpty()
                )
            }
        }
    }
}
