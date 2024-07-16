package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.optics.get
import io.github.edwinchang24.shengjidisplay.components.AppName
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
import io.github.edwinchang24.shengjidisplay.model.calls
import io.github.edwinchang24.shengjidisplay.model.found
import io.github.edwinchang24.shengjidisplay.model.possibleTrumps
import io.github.edwinchang24.shengjidisplay.model.trump
import io.github.edwinchang24.shengjidisplay.navigation.Dialog
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.navigation.Screen
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_add
import io.github.edwinchang24.shengjidisplay.resources.ic_clear_all
import io.github.edwinchang24.shengjidisplay.resources.ic_close
import io.github.edwinchang24.shengjidisplay.resources.ic_edit
import io.github.edwinchang24.shengjidisplay.resources.ic_info
import io.github.edwinchang24.shengjidisplay.resources.ic_settings
import io.github.edwinchang24.shengjidisplay.resources.ic_smart_display
import io.github.edwinchang24.shengjidisplay.util.WindowSize
import io.github.edwinchang24.shengjidisplay.util.calculateWindowSize
import io.github.edwinchang24.shengjidisplay.util.formatCallNumber
import io.github.edwinchang24.shengjidisplay.util.iconRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navigator: Navigator, state: AppState.Prop) {
    val windowSize = calculateWindowSize()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppName() },
                actions = {
                    IconButtonWithEmphasis(onClick = { navigator.toggleSettings() }) {
                        Icon(iconRes(Res.drawable.ic_settings), null)
                    }
                    IconButtonWithEmphasis(onClick = { navigator.navigate(Dialog.About) }) {
                        Icon(iconRes(Res.drawable.ic_info), null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            val large = windowSize == WindowSize.Large
            var startButtonsHeight by rememberSaveable { mutableStateOf(0) }
            val startButtonsHeightDp = with(LocalDensity.current) { startButtonsHeight.toDp() }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
                var tempTrumpRank by
                    rememberSaveable(state().trump) { mutableStateOf(state().trump?.rank) }
                var tempTrumpSuit by
                    rememberSaveable(state().trump) { mutableStateOf(state().trump?.suit) }
                LaunchedEffect(tempTrumpRank, tempTrumpSuit) {
                    tempTrumpRank?.let { r ->
                        tempTrumpSuit?.let { s -> state { AppState.trump set PlayingCard(r, s) } }
                    }
                }
                val cardColors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                val callsLayoutId = "calls"
                val cards =
                    @Composable {
                        PossibleTrumpsSelection(
                            cardColors,
                            windowSize,
                            navigator,
                            state,
                            modifier = Modifier.padding(12.dp)
                        )
                        TrumpCardSelection(
                            cardColors,
                            tempTrumpRank,
                            { tempTrumpRank = it },
                            tempTrumpSuit,
                            { tempTrumpSuit = it },
                            windowSize,
                            navigator,
                            state,
                            modifier = Modifier.padding(12.dp)
                        )
                        CallsSelection(
                            cardColors,
                            navigator,
                            state,
                            modifier = Modifier.padding(12.dp).layoutId(callsLayoutId)
                        )
                        TeammatesSelection(
                            cardColors,
                            navigator,
                            state,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                SubcomposeLayout(
                    modifier =
                        Modifier.verticalScroll(rememberScrollState())
                            .padding(12.dp)
                            .padding(bottom = if (!large) startButtonsHeightDp else 0.dp)
                ) { constraints ->
                    if (windowSize == WindowSize.Small) {
                        val placeables =
                            subcompose(0, cards).map {
                                it.measure(
                                    constraints.copy(
                                        minWidth = constraints.maxWidth,
                                        maxWidth = constraints.maxWidth
                                    )
                                )
                            }
                        layout(constraints.maxWidth, placeables.sumOf { it.height }) {
                            var y = 0
                            placeables.forEach {
                                it.placeRelative(0, y)
                                y += it.height
                            }
                        }
                    } else {
                        var slotId = 0
                        val placeablesFiltered =
                            subcompose(slotId++, cards)
                                .filterNot { it.layoutId == callsLayoutId }
                                .map { it.measure(constraints) }
                        val width =
                            minOf(constraints.maxWidth, placeablesFiltered.maxOf { it.width } * 2)
                        val placeablesWithFinalWidth =
                            subcompose(slotId++, cards).map {
                                it.measure(
                                    constraints.copy(minWidth = width / 2, maxWidth = width / 2)
                                )
                            }
                        val maxHeights =
                            placeablesWithFinalWidth.chunked(2).map { it.maxOf { p -> p.height } }
                        val placeablesFinal =
                            subcompose(slotId, cards).mapIndexed { index, m ->
                                m.measure(
                                    Constraints(
                                        minWidth = width / 2,
                                        maxWidth = width / 2,
                                        minHeight = maxHeights[index / 2],
                                        maxHeight = maxHeights[index / 2]
                                    )
                                )
                            }
                        layout(width, maxHeights.sum()) {
                            var y = 0
                            placeablesFinal.chunked(2).forEach { row ->
                                var x = 0
                                row.forEach {
                                    it.placeRelative(x, y)
                                    x += it.width
                                }
                                y += row.maxOf { it.height }
                            }
                        }
                    }
                }
                if (large) {
                    StartButtons(navigator, modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
            if (!large) {
                StartButtons(
                    navigator,
                    modifier =
                        Modifier.align(Alignment.BottomCenter)
                            .onGloballyPositioned { startButtonsHeight = it.size.height }
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun StartButtons(navigator: Navigator, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        ButtonWithEmphasis(
            text = "Start possible trumps display",
            icon = iconRes(Res.drawable.ic_smart_display),
            onClick = { navigator.navigate(Screen.Display(scheme = DisplayScheme.PossibleTrumps)) },
            elevation = ButtonDefaults.buttonElevation(6.dp, 6.dp, 6.dp, 8.dp, 6.dp)
        )
        ButtonWithEmphasis(
            text = "Start main display",
            icon = iconRes(Res.drawable.ic_smart_display),
            onClick = { navigator.navigate(Screen.Display(scheme = DisplayScheme.Main)) },
            elevation = ButtonDefaults.buttonElevation(6.dp, 6.dp, 6.dp, 8.dp, 6.dp)
        )
    }
}

@Composable
private fun PossibleTrumpsSelection(
    cardColors: CardColors,
    windowSize: WindowSize,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    val large = windowSize == WindowSize.Large
    Card(
        colors = cardColors,
        modifier =
            modifier
                .width(IntrinsicSize.Max)
                .clip(CardDefaults.shape)
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
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    AnimatedContent(
                        state().possibleTrumps,
                        modifier = Modifier.fillMaxWidth(),
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
                            modifier = Modifier.fillMaxWidth()
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    AnimatedContent(
                        state().possibleTrumps,
                        modifier = Modifier.weight(1f).padding(end = 16.dp)
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

@Composable
private fun TrumpCardSelection(
    cardColors: CardColors,
    tempTrumpRank: String?,
    setTempTrumpRank: (String) -> Unit,
    tempTrumpSuit: Suit?,
    setTempTrumpSuit: (Suit) -> Unit,
    windowSize: WindowSize,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    val large = windowSize == WindowSize.Large
    Card(
        colors = cardColors,
        modifier =
            modifier
                .width(IntrinsicSize.Max)
                .clip(CardDefaults.shape)
                .then(
                    if (!large)
                        Modifier.clickable { navigator.navigate(Dialog.EditTrump) }
                            .pointerHoverIcon(PointerIcon.Hand)
                    else Modifier
                )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            Text(
                "Trump card",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            if (large) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AnimatedContent(
                        targetState = state().trump,
                        modifier = Modifier.fillMaxWidth()
                    ) { targetTrump ->
                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            if (targetTrump != null) {
                                PlayingCard(
                                    targetTrump,
                                    state,
                                    textStyle = LocalTextStyle.current.copy(fontSize = 32.sp)
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
                AnimatedContent(targetState = state().trump) { targetTrump ->
                    if (targetTrump != null) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier =
                                Modifier.fillMaxWidth()
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
                                        state,
                                        modifier = Modifier.padding(8.dp).pressEmphasis(),
                                        textStyle = LocalTextStyle.current.copy(fontSize = 32.sp)
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
                                icon = iconRes(Res.drawable.ic_add),
                                onClick = { navigator.navigate(Dialog.EditTrump) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CallsSelection(
    cardColors: CardColors,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    Card(
        colors = cardColors,
        modifier =
            modifier
                .width(IntrinsicSize.Max)
                .clip(CardDefaults.shape)
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
                AnimatedVisibility(
                    visible = state().calls.isNotEmpty(),
                    enter =
                        fadeIn() +
                            expandVertically(expandFrom = Alignment.CenterVertically, clip = false),
                    exit =
                        fadeOut() +
                            shrinkVertically(
                                shrinkTowards = Alignment.CenterVertically,
                                clip = false
                            ),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(contentAlignment = Alignment.CenterEnd) {
                        OutlinedButtonWithEmphasis(
                            text = "Clear all",
                            icon = iconRes(Res.drawable.ic_clear_all),
                            onClick = { state { AppState.calls set emptyList() } }
                        )
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
                        icon = iconRes(Res.drawable.ic_add),
                        onClick = { navigator.navigate(Dialog.EditCall(0)) }
                    )
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

@Composable
private fun TeammatesSelection(
    cardColors: CardColors,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    Card(
        colors = cardColors,
        modifier =
            modifier
                .width(IntrinsicSize.Max)
                .clip(CardDefaults.shape)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    "${state().teammates.size} teammates added",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )
                OutlinedButtonWithEmphasis(
                    text = "Edit",
                    icon = iconRes(Res.drawable.ic_edit),
                    onClick = {
                        navigator.navigate(
                            Screen.Display(scheme = DisplayScheme.Main, editTeammates = true)
                        )
                    }
                )
            }
        }
    }
}
