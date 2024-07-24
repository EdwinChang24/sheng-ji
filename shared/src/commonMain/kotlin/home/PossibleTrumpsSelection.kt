package home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
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
import components.PossibleTrumpsPicker
import model.AppState
import model.possibleTrumps
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_edit
import util.ExpandWidths
import util.WeightRow
import util.WindowSize
import util.iconRes

@Composable
fun PossibleTrumpsSelection(
    cardColors: CardColors,
    windowSize: WindowSize,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    val large = windowSize == WindowSize.Large
    ExpandWidths(modifier = modifier) {
        Card(
            colors = cardColors,
            modifier =
                Modifier.clip(CardDefaults.shape)
                    .then(
                        if (!large)
                            Modifier.clickable { navigator.navigate(Dialog.EditPossibleTrumps) }
                                .pointerHoverIcon(PointerIcon.Hand)
                        else Modifier
                    )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Text(
                    "Possible trumps",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                if (large) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.expandWidth().padding(horizontal = 24.dp)
                    ) {
                        AnimatedContent(
                            state().possibleTrumps,
                            modifier = Modifier.expandWidth(),
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                        scaleIn(
                                            initialScale = 0.92f,
                                            animationSpec = tween(220, delayMillis = 90)
                                        ))
                                    .togetherWith(fadeOut(animationSpec = tween(90)))
                                    .using(SizeTransform())
                            }
                        ) { targetState ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.expandWidth()
                            ) {
                                Text(
                                    if (targetState.isEmpty()) "None selected"
                                    else targetState.joinToString(),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        PossibleTrumpsPicker(
                            selected = state().possibleTrumps,
                            setSelected = { state { AppState.possibleTrumps set it } }
                        )
                    }
                } else {
                    WeightRow(
                        modifier =
                            Modifier.expandWidth().padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        AnimatedContent(
                            state().possibleTrumps,
                            modifier = Modifier.weight().padding(end = 16.dp)
                        ) { targetState ->
                            Text(
                                if (targetState.isEmpty()) "None selected"
                                else targetState.joinToString(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        OutlinedButtonWithEmphasis(
                            text = "Edit",
                            icon = iconRes(Res.drawable.ic_edit),
                            onClick = { navigator.navigate(Dialog.EditPossibleTrumps) }
                        )
                    }
                }
            }
        }
    }
}
