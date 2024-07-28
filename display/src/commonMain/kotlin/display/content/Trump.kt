package display.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.OutlinedButtonWithEmphasis
import components.PlayingCard
import interaction.PressableWithEmphasis
import model.AppState
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_add
import util.DefaultTransition
import util.iconRes

@Composable
fun TrumpDisplay(
    state: AppState.Prop,
    navigator: Navigator,
    displayScale: Float,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = state().trump,
        transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
        modifier = modifier
    ) { targetTrump ->
        PressableWithEmphasis {
            targetTrump?.let {
                PlayingCard(
                    card = it,
                    state = state,
                    modifier =
                        Modifier.clip(MaterialTheme.shapes.large)
                            .then(
                                if (state().settings.mainDisplay.tapTrumpToEdit)
                                    Modifier.clickableForEmphasis(
                                        onClick = { navigator.navigate(Dialog.EditTrump) }
                                    )
                                else Modifier
                            )
                            .padding(24.dp)
                            .pressEmphasis(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 112.sp * displayScale)
                )
            }
                ?: Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier.clip(MaterialTheme.shapes.large)
                            .clickableForEmphasis(
                                onClick = { navigator.navigate(Dialog.EditTrump) }
                            )
                            .padding(24.dp)
                            .pressEmphasis()
                ) {
                    Text("No trump card selected")
                    OutlinedButtonWithEmphasis(
                        text = "Add",
                        icon = iconRes(Res.drawable.ic_add),
                        onClick = { navigator.navigate(Dialog.EditTrump) }
                    )
                }
        }
    }
}
