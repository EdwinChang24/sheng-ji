package home

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.unit.sp
import components.ButtonWithEmphasis
import components.IconButtonWithEmphasis
import components.PlayingCard
import components.RankPicker
import components.SuitPicker
import interaction.PressableWithEmphasis
import model.AppState
import model.Suit
import model.trump
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_add
import resources.ic_close
import util.WindowWidth
import util.iconRes

@Composable
fun TrumpCardSelection(
    cardColors: CardColors,
    tempTrumpRank: String?,
    setTempTrumpRank: (String) -> Unit,
    tempTrumpSuit: Suit?,
    setTempTrumpSuit: (Suit) -> Unit,
    windowWidth: WindowWidth,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = cardColors,
        modifier =
            modifier
                .clip(CardDefaults.shape)
                .then(
                    if (windowWidth < WindowWidth.Large)
                        Modifier.clickable { navigator.navigate(Dialog.EditTrump) }
                            .pointerHoverIcon(PointerIcon.Hand)
                    else Modifier
                ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 24.dp),
        ) {
            Text(
                "Trump card",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            if (windowWidth >= WindowWidth.Large) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AnimatedContent(
                        targetState = state().trump,
                        modifier = Modifier.fillMaxWidth(),
                    ) { targetTrump ->
                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp),
                        ) {
                            if (targetTrump != null) {
                                PlayingCard(
                                    targetTrump,
                                    state,
                                    textStyle = LocalTextStyle.current.copy(fontSize = 32.sp),
                                )
                                IconButtonWithEmphasis(
                                    onClick = { state { AppState.trump set null } }
                                ) {
                                    Icon(iconRes(Res.drawable.ic_close), null)
                                }
                            } else {
                                Text(
                                    "No trump card selected",
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    ) {
                        Text("Rank", style = MaterialTheme.typography.labelMedium)
                        RankPicker(
                            tempTrumpRank,
                            setTempTrumpRank,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    ) {
                        Text("Suit", style = MaterialTheme.typography.labelMedium)
                        SuitPicker(
                            tempTrumpSuit,
                            setTempTrumpSuit,
                            state,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    }
                }
            } else {
                AnimatedContent(targetState = state().trump) { targetTrump ->
                    if (targetTrump != null) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier =
                                Modifier.fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                        ) {
                            PressableWithEmphasis {
                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                        Modifier.clip(MaterialTheme.shapes.medium)
                                            .clickableForEmphasis {
                                                navigator.navigate(Dialog.EditTrump)
                                            }
                                            .padding(8.dp),
                                ) {
                                    PlayingCard(
                                        targetTrump,
                                        state,
                                        modifier = Modifier.padding(8.dp).pressEmphasis(),
                                        textStyle = LocalTextStyle.current.copy(fontSize = 32.sp),
                                    )
                                    IconButtonWithEmphasis(
                                        onClick = { state { AppState.trump set null } }
                                    ) {
                                        Icon(iconRes(Res.drawable.ic_close), null)
                                    }
                                }
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                        ) {
                            Text(
                                "No trump card selected",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f).padding(end = 16.dp),
                            )
                            ButtonWithEmphasis(
                                text = "Add",
                                icon = iconRes(Res.drawable.ic_add),
                                onClick = { navigator.navigate(Dialog.EditTrump) },
                            )
                        }
                    }
                }
            }
        }
    }
}
