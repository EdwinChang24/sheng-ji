package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.BuildConfig
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.appDestination
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.CallFoundText
import io.github.edwinchang24.shengjidisplay.components.IconButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.PlayingCard
import io.github.edwinchang24.shengjidisplay.destinations.DisplayPageDestination
import io.github.edwinchang24.shengjidisplay.destinations.EditCallDialogDestination
import io.github.edwinchang24.shengjidisplay.destinations.EditTrumpDialogDestination
import io.github.edwinchang24.shengjidisplay.destinations.SettingsPageDestination
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.util.formatCallNumber
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(style = HomePageTransitions::class)
@MainNavGraph(start = true)
@Composable
fun HomePage(
    navigator: DestinationsNavigator,
    editCallResultRecipient: ResultRecipient<EditCallDialogDestination, Int>,
    viewModel: MainActivityViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val state by
        viewModel.state.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButtonWithEmphasis(
                        onClick = { navigator.navigate(SettingsPageDestination) }
                    ) {
                        Icon(painterResource(R.drawable.ic_settings), null)
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
                                .clickable { navigator.navigate(EditTrumpDialogDestination) }
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
                                            navigator.navigate(EditTrumpDialogDestination)
                                        }
                                        .padding(8.dp)
                            ) {
                                PlayingCard(
                                    targetTrump,
                                    textStyle = LocalTextStyle.current.copy(fontSize = 32.sp),
                                    modifier = Modifier.padding(8.dp).pressEmphasis()
                                )
                                IconButtonWithEmphasis(
                                    onClick = { viewModel.state.value = state.copy(trump = null) }
                                ) {
                                    Icon(painterResource(R.drawable.ic_close), null)
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier.fillMaxWidth()
                                .clickable { navigator.navigate(EditTrumpDialogDestination) }
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text("No trump card selected")
                        Spacer(modifier = Modifier.weight(1f))
                        ButtonWithEmphasis(
                            onClick = { navigator.navigate(EditTrumpDialogDestination) }
                        ) {
                            Icon(painterResource(R.drawable.ic_add), null)
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
                        onClick = { viewModel.state.value = state.copy(calls = emptyList()) }
                    ) {
                        Icon(painterResource(R.drawable.ic_clear_all), null)
                        Text("Clear all")
                    }
                }
            }
            if (state.calls.isNotEmpty()) {
                val listState = rememberLazyListState()
                editCallResultRecipient.onNavResult {
                    if (it is NavResult.Value)
                        coroutineScope.launch { listState.animateScrollToItem(it.value) }
                }
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    itemsIndexed(state.calls) { index, call ->
                        CallCard(
                            call = call,
                            onEdit = { navigator.navigate(EditCallDialogDestination(index)) },
                            setFound = {
                                viewModel.state.value =
                                    state.copy(
                                        calls =
                                            state.calls.toMutableList().apply {
                                                this[index] = this[index].copy(found = it)
                                            }
                                    )
                            },
                            onDelete = {
                                viewModel.state.value =
                                    state.copy(
                                        calls =
                                            state.calls.toMutableList().apply { removeAt(index) }
                                    )
                            }
                        )
                    }
                    item {
                        OutlinedButtonWithEmphasis(
                            onClick = {
                                navigator.navigate(EditCallDialogDestination(state.calls.size))
                            }
                        ) {
                            Icon(painterResource(R.drawable.ic_add), null)
                            Text("Add call")
                        }
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier.fillMaxWidth()
                            .clickable { navigator.navigate(EditCallDialogDestination(0)) }
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("No calls added")
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonWithEmphasis(
                        onClick = { navigator.navigate(EditCallDialogDestination(0)) }
                    ) {
                        Icon(painterResource(R.drawable.ic_add), null)
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
                            navigator.navigate(DisplayPageDestination(editTeammates = true))
                        }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text("${state.teammates.size} teammates added")
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButtonWithEmphasis(
                    onClick = { navigator.navigate(DisplayPageDestination(editTeammates = true)) }
                ) {
                    Icon(painterResource(R.drawable.ic_edit), null)
                    Text("Edit")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ButtonWithEmphasis(
                onClick = { navigator.navigate(DisplayPageDestination()) },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 24.dp)
            ) {
                Icon(painterResource(R.drawable.ic_smart_display), null)
                Text("Start display")
            }
            Text(
                "Version ${BuildConfig.VERSION_NAME}",
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
                    Icon(painterResource(R.drawable.ic_close), null)
                }
            }
        }
    }
}

object HomePageTransitions : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() =
        when (initialState.appDestination()) {
            DisplayPageDestination -> fadeIn(tween(delayMillis = 250))
            else -> fadeIn()
        }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() =
        when (targetState.appDestination()) {
            DisplayPageDestination -> fadeOut(tween(durationMillis = 250))
            else -> fadeOut()
        }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition() =
        when (initialState.appDestination()) {
            DisplayPageDestination -> fadeIn(tween(delayMillis = 250))
            else -> fadeIn()
        }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() =
        when (targetState.appDestination()) {
            DisplayPageDestination -> fadeOut(tween(durationMillis = 250))
            else -> fadeOut()
        }
}
