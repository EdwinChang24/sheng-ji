package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.edwinchang24.shengjidisplay.VersionConfig
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.CallFoundText
import io.github.edwinchang24.shengjidisplay.components.IconButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.PlayingCard
import io.github.edwinchang24.shengjidisplay.components.PossibleTrumpsPicker
import io.github.edwinchang24.shengjidisplay.components.RankPicker
import io.github.edwinchang24.shengjidisplay.components.SuitPicker
import io.github.edwinchang24.shengjidisplay.display.DisplayScheme
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.model.PlayingCard
import io.github.edwinchang24.shengjidisplay.model.Suit
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

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun HomePage(navigator: Navigator, state: AppState, setState: (AppState) -> Unit) {
    val windowWidthSizeClass = calculateWindowSizeClass().widthSizeClass
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(Res.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
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
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
        ) {
            var tempTrumpRank by rememberSaveable(state.trump) { mutableStateOf(state.trump?.rank) }
            var tempTrumpSuit by rememberSaveable(state.trump) { mutableStateOf(state.trump?.suit) }
            LaunchedEffect(tempTrumpRank, tempTrumpSuit) {
                tempTrumpRank?.let { r ->
                    tempTrumpSuit?.let { s -> setState(state.copy(trump = PlayingCard(r, s))) }
                }
            }
            val cardColors =
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(colors = cardColors, modifier = Modifier.height(IntrinsicSize.Max)) {
                        PossibleTrumpsSelection(windowWidthSizeClass, navigator, state, setState)
                    }
                    Card(colors = cardColors, modifier = Modifier.height(IntrinsicSize.Max)) {
                        TrumpCardSelection(
                            tempTrumpRank,
                            { tempTrumpRank = it },
                            tempTrumpSuit,
                            { tempTrumpSuit = it },
                            windowWidthSizeClass,
                            navigator,
                            state,
                            setState
                        )
                    }
                    Card(colors = cardColors, modifier = Modifier.height(IntrinsicSize.Max)) {
                        CallsSelection(navigator, state, setState)
                    }
                    Card(colors = cardColors, modifier = Modifier.height(IntrinsicSize.Max)) {
                        TeammatesSelection(navigator, state)
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        (if (windowWidthSizeClass == WindowWidthSizeClass.Expanded)
                                Modifier.width(IntrinsicSize.Max)
                            else Modifier.fillMaxWidth())
                            .then(Modifier.align(Alignment.CenterHorizontally))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
                    ) {
                        Card(colors = cardColors, modifier = Modifier.weight(1f).fillMaxHeight()) {
                            PossibleTrumpsSelection(
                                windowWidthSizeClass,
                                navigator,
                                state,
                                setState
                            )
                        }
                        Card(colors = cardColors, modifier = Modifier.weight(1f).fillMaxHeight()) {
                            TrumpCardSelection(
                                tempTrumpRank,
                                { tempTrumpRank = it },
                                tempTrumpSuit,
                                { tempTrumpSuit = it },
                                windowWidthSizeClass,
                                navigator,
                                state,
                                setState
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
                    ) {
                        Card(colors = cardColors, modifier = Modifier.weight(1f).fillMaxHeight()) {
                            CallsSelection(navigator, state, setState)
                        }
                        Card(colors = cardColors, modifier = Modifier.weight(1f).fillMaxHeight()) {
                            TeammatesSelection(navigator, state)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                ButtonWithEmphasis(
                    text = "Start possible trumps display",
                    icon = painterResource(Res.drawable.ic_smart_display),
                    onClick = {
                        navigator.navigate(Screen.Display(scheme = DisplayScheme.PossibleTrumps))
                    }
                )
                ButtonWithEmphasis(
                    text = "Start main display",
                    icon = painterResource(Res.drawable.ic_smart_display),
                    onClick = { navigator.navigate(Screen.Display(scheme = DisplayScheme.Main)) }
                )
            }
            Text(
                "Version ${VersionConfig.version}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun PossibleTrumpsSelection(
    windowWidthSizeClass: WindowWidthSizeClass,
    navigator: Navigator,
    state: AppState,
    setState: (AppState) -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = windowWidthSizeClass == WindowWidthSizeClass.Expanded
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .then(
                    if (!expanded)
                        Modifier.clickable { navigator.navigate(Dialog.EditPossibleTrumps) }
                    else Modifier
                )
                .fillMaxHeight()
                .padding(vertical = 24.dp)
    ) {
        Text(
            "Possible trumps",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        if (expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                AnimatedContent(state.possibleTrumps, modifier = Modifier.fillMaxWidth()) {
                    targetState ->
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
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
                    selected = state.possibleTrumps,
                    setSelected = { setState(state.copy(possibleTrumps = it)) }
                )
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                AnimatedContent(
                    state.possibleTrumps,
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                ) { targetState ->
                    Text(
                        if (targetState.isEmpty()) "None selected" else targetState.joinToString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                OutlinedButtonWithEmphasis(
                    text = "Edit",
                    icon = painterResource(Res.drawable.ic_edit),
                    onClick = { navigator.navigate(Dialog.EditPossibleTrumps) }
                )
            }
        }
    }
}

@Composable
private fun TrumpCardSelection(
    tempTrumpRank: String?,
    setTempTrumpRank: (String) -> Unit,
    tempTrumpSuit: Suit?,
    setTempTrumpSuit: (Suit) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    navigator: Navigator,
    state: AppState,
    setState: (AppState) -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = windowWidthSizeClass == WindowWidthSizeClass.Expanded
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .then(
                    if (!expanded) Modifier.clickable { navigator.navigate(Dialog.EditTrump) }
                    else Modifier
                )
                .fillMaxHeight()
                .padding(vertical = 24.dp)
    ) {
        Text(
            "Trump card",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        if (expanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(targetState = state.trump, modifier = Modifier.fillMaxWidth()) {
                    targetTrump ->
                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (targetTrump != null) {
                            PlayingCard(
                                targetTrump,
                                textStyle = LocalTextStyle.current.copy(fontSize = 32.sp)
                            )
                            IconButtonWithEmphasis(
                                onClick = { setState(state.copy(trump = null)) }
                            ) {
                                Icon(painterResource(Res.drawable.ic_close), null)
                            }
                        } else {
                            Text(
                                "No trump card selected",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Text("Rank", style = MaterialTheme.typography.labelMedium)
                    RankPicker(
                        tempTrumpRank,
                        setTempTrumpRank,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Text("Suit", style = MaterialTheme.typography.labelMedium)
                    SuitPicker(
                        tempTrumpSuit,
                        setTempTrumpSuit,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        } else {
            AnimatedContent(targetState = state.trump) { targetTrump ->
                if (targetTrump != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)
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
                            Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "No trump card selected",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        ButtonWithEmphasis(
                            text = "Add",
                            icon = painterResource(Res.drawable.ic_add),
                            onClick = { navigator.navigate(Dialog.EditTrump) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CallsSelection(
    navigator: Navigator,
    state: AppState,
    setState: (AppState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .then(
                    if (state.calls.isEmpty())
                        Modifier.clickable { navigator.navigate(Dialog.EditCall(0)) }
                    else Modifier
                )
                .fillMaxHeight()
                .padding(vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Text(
                "Calls",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            if (state.calls.isNotEmpty()) {
                OutlinedButtonWithEmphasis(
                    text = "Clear all",
                    icon = painterResource(Res.drawable.ic_clear_all),
                    onClick = { setState(state.copy(calls = emptyList())) }
                )
            }
        }
        if (state.calls.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.padding(bottom = 8.dp)
                        .then(
                            object : LayoutModifier {
                                override fun MeasureScope.measure(
                                    measurable: Measurable,
                                    constraints: Constraints
                                ) =
                                    with(measurable.measure(constraints)) {
                                        layout(width, height) { placeRelative(0, 0) }
                                    }

                                override fun IntrinsicMeasureScope.maxIntrinsicWidth(
                                    measurable: IntrinsicMeasurable,
                                    height: Int
                                ) = 200.dp.roundToPx()
                            }
                        )
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                state.calls.forEachIndexed { index, call ->
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
                                    calls = state.calls.toMutableList().apply { removeAt(index) }
                                )
                            )
                        }
                    )
                }
                OutlinedButtonWithEmphasis(
                    text = "Add call",
                    icon = painterResource(Res.drawable.ic_add),
                    onClick = { navigator.navigate(Dialog.EditCall(state.calls.size)) }
                )
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    "No calls added",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )
                ButtonWithEmphasis(
                    text = "Add",
                    icon = painterResource(Res.drawable.ic_add),
                    onClick = { navigator.navigate(Dialog.EditCall(0)) }
                )
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

@Composable
private fun TeammatesSelection(
    navigator: Navigator,
    state: AppState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .clickable {
                    navigator.navigate(
                        Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                    )
                }
                .fillMaxHeight()
                .padding(vertical = 24.dp)
    ) {
        Text(
            "Teammates",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                "${state.teammates.size} teammates added",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedButtonWithEmphasis(
                text = "Edit",
                icon = painterResource(Res.drawable.ic_edit),
                onClick = {
                    navigator.navigate(
                        Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                    )
                }
            )
        }
    }
}
