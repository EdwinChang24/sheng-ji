package home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.benasher44.uuid.uuid4
import components.ButtonWithEmphasis
import components.CallFoundText
import components.IconButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import components.PlayingCard
import interaction.PressableWithEmphasis
import model.AppState
import model.Call
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_add
import resources.ic_clear_all
import resources.ic_close
import resources.ic_undo
import sh.calvin.reorderable.ReorderableRow
import sh.calvin.reorderable.ReorderableScope
import util.ClearableState
import util.DefaultTransition
import util.ExpandWidths
import util.formatCallNumber
import util.iconRes

@Composable
fun CallsSelection(
    callsState: ClearableState<List<Call>>,
    cardColors: CardColors,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    Card(colors = cardColors, modifier = modifier.clip(CardDefaults.shape)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Text(
                "Calls",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            AnimatedContent(
                targetState = callsState.value,
                transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
                contentKey = { it.isEmpty() }
            ) { calls ->
                if (calls.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier.horizontalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        ReorderableRow(
                            list = calls,
                            onSettle = { from, to ->
                                callsState.setValue(
                                    callsState.value.toMutableList().apply {
                                        add(to, removeAt(from))
                                    }
                                )
                            },
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) { _, call, _ ->
                            key(call.id) {
                                CallCard(
                                    call = call,
                                    onEdit = { navigator.navigate(Dialog.EditCall(call.id)) },
                                    setFound = { found ->
                                        callsState.setValue(
                                            callsState.value.map {
                                                if (it.id == call.id) it.copy(found = found) else it
                                            }
                                        )
                                    },
                                    onDelete = {
                                        callsState.setValue(
                                            callsState.value.filter { it.id != call.id }
                                        )
                                    },
                                    state = state
                                )
                            }
                        }
                        OutlinedButtonWithEmphasis(
                            text = "Add call",
                            icon = iconRes(Res.drawable.ic_add),
                            onClick = { navigator.navigate(Dialog.EditCall(uuid4().toString())) },
                            modifier = Modifier.zIndex(-1f)
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier.clickable {
                                    navigator.navigate(Dialog.EditCall(uuid4().toString()))
                                }
                                .pointerHoverIcon(PointerIcon.Hand)
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        ) {
                            Text("No calls added", maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        ButtonWithEmphasis(
                            text = "Add",
                            icon = iconRes(Res.drawable.ic_add),
                            onClick = { navigator.navigate(Dialog.EditCall(uuid4().toString())) }
                        )
                    }
                }
            }
            OutlinedButtonWithEmphasis(
                text = if (callsState.canUndoClear) "Undo clear" else "Clear",
                icon =
                    iconRes(
                        if (callsState.canUndoClear) Res.drawable.ic_undo
                        else Res.drawable.ic_clear_all
                    ),
                onClick = {
                    if (callsState.canUndoClear) callsState.undoClearValue()
                    else callsState.clearValue()
                },
                enabled = callsState.canUndoClear || callsState.value.isNotEmpty(),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
private fun ReorderableScope.CallCard(
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
            modifier =
                modifier
                    .longPressDraggableHandle(interactionSource = interactionSource)
                    .pointerHoverIcon(PointerIcon.Hand)
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
                        AnimatedContent(
                            targetState = call.card,
                            transitionSpec = { DefaultTransition using SizeTransform(clip = false) }
                        ) { card ->
                            PlayingCard(
                                card,
                                state = state,
                                textStyle = LocalTextStyle.current.copy(fontSize = 32.sp)
                            )
                        }
                        AnimatedContent(
                            targetState = call.number,
                            transitionSpec = { DefaultTransition using SizeTransform(clip = false) }
                        ) { number ->
                            Text(formatCallNumber(number))
                        }
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
