package home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import model.AppState
import navigation.Navigator
import navigation.Screen
import resources.Res
import resources.ic_edit
import util.ExpandHeights
import util.ExpandWidths
import util.WeightRow
import util.iconRes

@Composable
fun TeammatesSelection(
    cardColors: CardColors,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    ExpandWidths(modifier = modifier) {
        Card(
            colors = cardColors,
            modifier =
                Modifier.clip(CardDefaults.shape)
                    .clickable {
                        navigator.navigate(
                            Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                        )
                    }
                    .pointerHoverIcon(PointerIcon.Hand)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Text(
                    "Teammates",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                ExpandHeights {
                    WeightRow(
                        modifier =
                            Modifier.expandWidth().padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.expandHeight().weight()
                        ) {
                            Text(
                                "${state().teammates.size} teammates added",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.expandHeight()
                        ) {
                            OutlinedButtonWithEmphasis(
                                text = "Edit",
                                icon = iconRes(Res.drawable.ic_edit),
                                onClick = {
                                    navigator.navigate(
                                        Screen.Display(
                                            scheme = DisplayScheme.Main,
                                            editTeammates = true
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
