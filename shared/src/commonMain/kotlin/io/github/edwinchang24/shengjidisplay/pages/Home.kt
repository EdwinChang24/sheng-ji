package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.edwinchang24.shengjidisplay.VersionConfig
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.CallFoundText
import io.github.edwinchang24.shengjidisplay.components.IconButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.PlayingCard
import io.github.edwinchang24.shengjidisplay.display.DisplayScheme
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.navigation.Dialog
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.navigation.Screen
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.app_name
import io.github.edwinchang24.shengjidisplay.resources.ic_add
import io.github.edwinchang24.shengjidisplay.resources.ic_clear_all
import io.github.edwinchang24.shengjidisplay.resources.ic_close
import io.github.edwinchang24.shengjidisplay.resources.ic_edit
import io.github.edwinchang24.shengjidisplay.resources.ic_settings
import io.github.edwinchang24.shengjidisplay.resources.ic_smart_display
import io.github.edwinchang24.shengjidisplay.util.formatCallNumber
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navigator: Navigator, state: AppState, setState: (AppState) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                actions = {
                    IconButtonWithEmphasis(onClick = { navigator.toggleSettings() }) {
                        Icon(painterResource(Res.drawable.ic_settings), null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
                Modifier.fillMaxSize()
                    .padding(padding)
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Possible trumps",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.fillMaxWidth()
                        .clickable { navigator.navigate(Dialog.EditPossibleTrumps) }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    if (state.possibleTrumps.isEmpty()) "None selected"
                    else state.possibleTrumps.joinToString(),
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )
                OutlinedButtonWithEmphasis(
                    onClick = { navigator.navigate(Dialog.EditPossibleTrumps) }
                ) {
                    Icon(painterResource(Res.drawable.ic_edit), null)
                    Text("Edit")
                }
            }
            Text(
                "Trump card",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            AnimatedContent(targetState = state.trump, label = "") { targetTrump ->
                if (targetTrump != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier.fillMaxWidth()
                                .clickable { navigator.navigate(Dialog.EditTrump) }
                                .padding(horizontal = 24.dp, vertical = 16.dp)
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
                                        .padding(8.dp)
                            ) {
                                PlayingCard(
                                    targetTrump,
                                    textStyle = LocalTextStyle.current.copy(fontSize = 32.sp),
                                    modifier = Modifier.padding(8.dp).pressEmphasis()
                                )
                                IconButtonWithEmphasis(
                                    onClick = { setState(state.copy(trump = null)) }
                                ) {
                                    Icon(painterResource(Res.drawable.ic_close), null)
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier.fillMaxWidth()
                                .clickable { navigator.navigate(Dialog.EditTrump) }
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text("No trump card selected")
                        Spacer(modifier = Modifier.weight(1f))
                        ButtonWithEmphasis(onClick = { navigator.navigate(Dialog.EditTrump) }) {
                            Icon(painterResource(Res.drawable.ic_add), null)
                            Text("Add")
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                Text("Calls", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                AnimatedVisibility(
                    visible = state.calls.isNotEmpty(),
                    enter =
                        fadeIn() +
                            expandVertically(expandFrom = Alignment.CenterVertically, clip = false),
                    exit =
                        fadeOut() +
                            shrinkVertically(
                                shrinkTowards = Alignment.CenterVertically,
                                clip = false
                            )
                ) {
                    OutlinedButtonWithEmphasis(
                        onClick = { setState(state.copy(calls = emptyList())) }
                    ) {
                        Icon(painterResource(Res.drawable.ic_clear_all), null)
                        Text("Clear all")
                    }
                }
            }
            if (state.calls.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    itemsIndexed(state.calls) { index, call ->
                        CallCard(
                            call = call,
                            onEdit = { navigator.navigate(Dialog.EditCall(index)) },
                            setFound = {
                                setState(
                                    state.copy(
                                        calls =
                                            state.calls.toMutableList().apply {
                                                this[index] = this[index].copy(found = it)
                                            }
                                    )
                                )
                            },
                            onDelete = {
                                setState(
                                    state.copy(
                                        calls =
                                            state.calls.toMutableList().apply { removeAt(index) }
                                    )
                                )
                            }
                        )
                    }
                    item {
                        OutlinedButtonWithEmphasis(
                            onClick = { navigator.navigate(Dialog.EditCall(state.calls.size)) }
                        ) {
                            Icon(painterResource(Res.drawable.ic_add), null)
                            Text("Add call")
                        }
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier.fillMaxWidth()
                            .clickable { navigator.navigate(Dialog.EditCall(0)) }
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("No calls added")
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonWithEmphasis(onClick = { navigator.navigate(Dialog.EditCall(0)) }) {
                        Icon(painterResource(Res.drawable.ic_add), null)
                        Text("Add")
                    }
                }
            }
            Text(
                "Teammates",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.fillMaxWidth()
                        .clickable {
                            navigator.navigate(
                                Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                            )
                        }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text("${state.teammates.size} teammates added")
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButtonWithEmphasis(
                    onClick = {
                        navigator.navigate(
                            Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                        )
                    }
                ) {
                    Icon(painterResource(Res.drawable.ic_edit), null)
                    Text("Edit")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                ButtonWithEmphasis(
                    onClick = {
                        navigator.navigate(Screen.Display(scheme = DisplayScheme.PossibleTrumps))
                    }
                ) {
                    Icon(painterResource(Res.drawable.ic_smart_display), null)
                    Text("Start possible trumps display")
                }
                ButtonWithEmphasis(
                    onClick = { navigator.navigate(Screen.Display(scheme = DisplayScheme.Main)) }
                ) {
                    Icon(painterResource(Res.drawable.ic_smart_display), null)
                    Text("Start main display")
                }
            }
            Text(
                "Version ${VersionConfig.version}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun CallCard(
    call: Call,
    onEdit: () -> Unit,
    setFound: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    PressableWithEmphasis {
        OutlinedCard(onClick = onEdit, interactionSource = interactionSource, modifier = modifier) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentWidth().padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().pressEmphasis()
                ) {
                    PlayingCard(
                        call.card,
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
                    Icon(painterResource(Res.drawable.ic_close), null)
                }
            }
        }
    }
}
