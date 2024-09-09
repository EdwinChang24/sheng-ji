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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_edit
import util.ClearableState
import util.WindowWidth
import util.iconRes

@Composable
fun PossibleTrumpsSelection(
    possibleTrumpsState: ClearableState<Set<String>>,
    cardColors: CardColors,
    windowWidth: WindowWidth,
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = cardColors,
        modifier =
            modifier
                .clip(CardDefaults.shape)
                .then(
                    if (windowWidth < WindowWidth.Large)
                        Modifier.clickable { navigator.navigate(Dialog.EditPossibleTrumps) }
                            .pointerHoverIcon(PointerIcon.Hand)
                    else Modifier
                ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 24.dp),
        ) {
            Text(
                "Possible trumps",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            if (windowWidth >= WindowWidth.Large) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                ) {
                    AnimatedContent(
                        possibleTrumpsState.value,
                        modifier = Modifier.fillMaxWidth(),
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                    scaleIn(
                                        initialScale = 0.92f,
                                        animationSpec = tween(220, delayMillis = 90),
                                    ))
                                .togetherWith(fadeOut(animationSpec = tween(90)))
                                .using(SizeTransform())
                        },
                    ) { targetState ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                if (targetState.isEmpty()) "None selected"
                                else targetState.joinToString(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                    }
                    PossibleTrumpsPicker(possibleTrumpsState)
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                ) {
                    AnimatedContent(
                        possibleTrumpsState.value,
                        modifier = Modifier.weight(1f).padding(end = 16.dp),
                    ) { targetState ->
                        Text(
                            if (targetState.isEmpty()) "None selected"
                            else targetState.joinToString(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 16.dp),
                        )
                    }
                    OutlinedButtonWithEmphasis(
                        text = "Edit",
                        icon = iconRes(Res.drawable.ic_edit),
                        onClick = { navigator.navigate(Dialog.EditPossibleTrumps) },
                    )
                }
            }
        }
    }
}
