package home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import arrow.optics.get
import components.ButtonWithEmphasis
import components.CallFoundText
import components.IconButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import components.PlayingCard
import interaction.PressableWithEmphasis
import model.AppState
import model.Call
import model.calls
import model.found
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_add
import resources.ic_clear_all
import resources.ic_close
import util.ExpandHeights
import util.ExpandWidths
import util.WeightRow
import util.formatCallNumber
import util.iconRes

@Composable
fun CallsSelection(
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
                    .then(
                        if (state().calls.isEmpty())
                            Modifier.clickable { navigator.navigate(Dialog.EditCall(0)) }
                                .pointerHoverIcon(PointerIcon.Hand)
                        else Modifier
                    )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                ExpandHeights {
                    WeightRow(modifier = Modifier.expandWidth().padding(horizontal = 24.dp)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.expandHeight()
                        ) {
                            Text(
                                "Calls",
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        AnimatedVisibility(
                            visible = state().calls.isNotEmpty(),
                            enter =
                                fadeIn() +
                                    expandVertically(
                                        expandFrom = Alignment.CenterVertically,
                                        clip = false
                                    ),
                            exit =
                                fadeOut() +
                                    shrinkVertically(
                                        shrinkTowards = Alignment.CenterVertically,
                                        clip = false
                                    ),
                            modifier = Modifier.weight().expandHeight()
                        ) {
                            Box(
                                contentAlignment = Alignment.CenterEnd,
                                modifier = Modifier.expandHeight()
                            ) {
                                OutlinedButtonWithEmphasis(
                                    text = "Clear all",
                                    icon = iconRes(Res.drawable.ic_clear_all),
                                    onClick = { state { AppState.calls set emptyList() } }
                                )
                            }
                        }
                    }
                }
                if (state().calls.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier.padding(bottom = 8.dp)
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        state().calls.forEachIndexed { index, call ->
                            CallCard(
                                call = call,
                                onEdit = { navigator.navigate(Dialog.EditCall(index)) },
                                setFound = { state { AppState.calls[index].found set it } },
                                onDelete = {
                                    state {
                                        AppState.calls.transform {
                                            it.toMutableList().apply { removeAt(index) }
                                        }
                                    }
                                },
                                state = state
                            )
                        }
                        OutlinedButtonWithEmphasis(
                            text = "Add call",
                            icon = iconRes(Res.drawable.ic_add),
                            onClick = { navigator.navigate(Dialog.EditCall(state().calls.size)) }
                        )
                    }
                } else {
                    ExpandHeights {
                        WeightRow(
                            modifier =
                                Modifier.expandWidth().padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier.expandHeight().weight().padding(end = 16.dp)
                            ) {
                                Text(
                                    "No calls added",
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.expandHeight()
                            ) {
                                ButtonWithEmphasis(
                                    text = "Add",
                                    icon = iconRes(Res.drawable.ic_add),
                                    onClick = { navigator.navigate(Dialog.EditCall(0)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CallCard(
    call: Call,
    onEdit: () -> Unit,
    setFound: (Int) -> Unit,
    onDelete: () -> Unit,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    PressableWithEmphasis {
        OutlinedCard(
            onClick = onEdit,
            colors =
                CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
            interactionSource = interactionSource,
            modifier = modifier
        ) {
            ExpandWidths {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.expandWidth().pressEmphasis()
                    ) {
                        PlayingCard(
                            call.card,
                            state = state,
                            textStyle = LocalTextStyle.current.copy(fontSize = 32.sp)
                        )
                        Text(formatCallNumber(call.number))
                    }
                    PressableWithEmphasis {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.clip(MaterialTheme.shapes.small)
                                    .clickableForEmphasis(
                                        onLongClick = {
                                            setFound((call.found + call.number) % (call.number + 1))
                                        },
                                        onClick = { setFound((call.found + 1) % (call.number + 1)) }
                                    )
                                    .padding(8.dp)
                                    .pressEmphasis()
                        ) {
                            Text("Found:")
                            Spacer(modifier = Modifier.width(8.dp))
                            CallFoundText(call = call)
                        }
                    }
                    IconButtonWithEmphasis(onClick = onDelete) {
                        Icon(iconRes(Res.drawable.ic_close), null)
                    }
                }
            }
        }
    }
}
