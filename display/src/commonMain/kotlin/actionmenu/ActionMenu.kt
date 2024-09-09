package actionmenu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import interaction.PressableWithEmphasis
import kotlin.math.absoluteValue
import resources.Res
import resources.ic_action_menu
import resources.ic_close
import util.WindowHeight
import util.calculateWindowHeight
import util.iconRes

val ActionMenuButtonSize = 56.dp
private val ButtonSize = 100.dp
private val ButtonSizeWithPadding = ButtonSize + 8.dp

private data class ActionButtonDef(val action: Action, val x: Dp = 0.dp, val y: Dp = 0.dp)

@Composable
fun ActionMenu(
    onAction: (Action) -> Unit,
    canPause: Boolean,
    pause: Boolean,
    showTeammates: Boolean,
    editingTeammates: Boolean,
    modifier: Modifier = Modifier,
) {
    val windowHeight = calculateWindowHeight()
    var menuState by
        rememberSaveable(stateSaver = ActionMenuState.StateSaver) {
            mutableStateOf(ActionMenuState.Closed())
        }
    AnimatedVisibility(
        visible = menuState !is ActionMenuState.Closed,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .alpha(0.75f)
                    .background(MaterialTheme.colorScheme.surfaceDim)
                    .pointerInput(true) {}
        )
    }
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val width = maxWidth
        val height = maxHeight
        var hintedAction by
            rememberSaveable(menuState is ActionMenuState.Closed, stateSaver = Action.StateSaver) {
                mutableStateOf(null)
            }
        val actionButtons =
            listOf(
                ActionButtonDef(Action.Teammates(showTeammates), x = -ButtonSizeWithPadding),
                ActionButtonDef(Action.Exit, x = ButtonSizeWithPadding),
                ActionButtonDef(
                    Action.PauseResume(canPause, pause),
                    x = -ButtonSizeWithPadding / 2,
                    y = -ButtonSizeWithPadding,
                ),
                ActionButtonDef(
                    Action.Settings,
                    x = ButtonSizeWithPadding / 2,
                    y = -ButtonSizeWithPadding,
                ),
                ActionButtonDef(
                    Action.Rotate,
                    x = -ButtonSizeWithPadding / 2,
                    y = ButtonSizeWithPadding,
                ),
                ActionButtonDef(
                    Action.Scale,
                    x = ButtonSizeWithPadding / 2,
                    y = ButtonSizeWithPadding,
                ),
            )
        val actionButtonsEnabled = actionButtons.associate { it.action to it.action.enabled() }
        fun getDragSelection() =
            (menuState as? ActionMenuState.Dragging)?.let { state ->
                val (x, y) = with(density) { state.offset.x.toDp() to state.offset.y.toDp() }
                actionButtons
                    .firstOrNull { (_, x1, y1) ->
                        (x - x1).value.absoluteValue <= ButtonSizeWithPadding.value / 2 &&
                            (y - y1).value.absoluteValue <= ButtonSizeWithPadding.value / 2
                    }
                    ?.action
                    ?.takeIf { actionButtonsEnabled[it] == true }
            }
        getDragSelection().let { LaunchedEffect(it) { if (it != null) hintedAction = it } }
        var key by rememberSaveable { mutableStateOf(0) }
        key(key) {
            val dragSelection = getDragSelection()
            var useInitialState by rememberSaveable { mutableStateOf(true) }
            LaunchedEffect(true) { useInitialState = false }
            actionButtons.forEach { actionButton ->
                val definedOffset =
                    with(LocalDensity.current) {
                        IntOffset(actionButton.x.roundToPx(), actionButton.y.roundToPx())
                    }
                val offset =
                    animateIntOffsetAsState(
                        if (useInitialState) IntOffset.Zero
                        else
                            when (val state = menuState) {
                                is ActionMenuState.Closed ->
                                    if (state.action == actionButton.action) definedOffset
                                    else IntOffset.Zero
                                is ActionMenuState.Dragging,
                                ActionMenuState.Opened -> definedOffset
                            },
                        tween(200),
                    )
                val scale =
                    animateFloatAsState(
                        if (useInitialState) 0f
                        else
                            when (val state = menuState) {
                                is ActionMenuState.Closed ->
                                    if (state.action == actionButton.action) 1f else 0f
                                is ActionMenuState.Dragging,
                                ActionMenuState.Opened -> 1f
                            },
                        tween(200),
                    )
                val alpha =
                    animateFloatAsState(
                        if (useInitialState || menuState is ActionMenuState.Closed) 0f else 1f,
                        tween(
                            (menuState as? ActionMenuState.Closed)?.let {
                                if (it.action == actionButton.action) 600 else null
                            } ?: 200
                        ),
                    )
                ActionButton(
                    actionButton.action,
                    onClick = {
                        menuState = ActionMenuState.Closed(actionButton.action)
                        onAction(actionButton.action)
                    },
                    menuState = menuState,
                    onPress = { hintedAction = actionButton.action },
                    dragSelected = dragSelection == actionButton.action,
                    modifier =
                        Modifier.offset { IntOffset(width.roundToPx() / 2, height.roundToPx() / 2) }
                            .size(ButtonSize)
                            .offset(x = -ButtonSize / 2, y = -ButtonSize / 2)
                            .offset { offset.value }
                            .scale(scale.value)
                            .alpha(alpha.value)
                            .zIndex(1f),
                )
            }
        }
        val interactionSource = remember { MutableInteractionSource() }
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier.offset { IntOffset(width.roundToPx() / 2, height.roundToPx() / 2) }
                    .size(ActionMenuButtonSize)
                    .offset(x = -ActionMenuButtonSize / 2, y = -ActionMenuButtonSize / 2)
                    .alpha(animateFloatAsState(if (editingTeammates) 0f else 1f).value)
                    .scale(animateFloatAsState(if (editingTeammates) 0f else 1f).value)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .zIndex(2f)
                    .then(
                        if (editingTeammates) Modifier
                        else {
                            Modifier.hoverable(interactionSource)
                                .pointerInput(true) {
                                    detectTapGestures(
                                        onPress = { offset ->
                                            if (offset.isUnspecified) return@detectTapGestures
                                            if (menuState is ActionMenuState.Closed) {
                                                menuState =
                                                    ActionMenuState.Dragging(
                                                        offset.round() +
                                                            with(density) {
                                                                IntOffset(
                                                                    -ActionMenuButtonSize
                                                                        .roundToPx() / 2,
                                                                    -ActionMenuButtonSize
                                                                        .roundToPx() / 2,
                                                                )
                                                            }
                                                    )
                                                key++
                                            }
                                        },
                                        onTap = {
                                            menuState =
                                                if (menuState == ActionMenuState.Opened)
                                                    ActionMenuState.Closed()
                                                else ActionMenuState.Opened
                                        },
                                    )
                                }
                                .pointerInput(canPause, showTeammates) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            val dragSelection = getDragSelection()
                                            menuState =
                                                if (dragSelection != null)
                                                    ActionMenuState.Closed(dragSelection).also {
                                                        onAction(dragSelection)
                                                    }
                                                else ActionMenuState.Opened
                                        },
                                        onDragCancel = { menuState = ActionMenuState.Opened },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            if (dragAmount.isUnspecified) return@detectDragGestures
                                            (menuState as? ActionMenuState.Dragging)?.let {
                                                menuState =
                                                    it.copy(offset = it.offset + dragAmount.round())
                                            }
                                        },
                                    )
                                }
                                .focusable(interactionSource = interactionSource)
                                .pointerHoverIcon(PointerIcon.Hand)
                        }
                    ),
        ) {
            AnimatedContent(
                menuState == ActionMenuState.Opened,
                transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut() },
                modifier = Modifier.fillMaxSize(),
            ) { opened ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        iconRes(if (opened) Res.drawable.ic_close else Res.drawable.ic_action_menu),
                        null,
                    )
                }
            }
        }
        if (windowHeight >= WindowHeight.Large) {
            AnimatedContent(
                targetState = hintedAction,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier.fillMaxSize(),
            ) { targetState ->
                BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                    targetState?.let {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier =
                                Modifier.widthIn(max = maxWidth * 0.6f).align(Alignment.TopCenter),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(iconRes(it.icon()), null)
                                Text(
                                    it.name(),
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            Text(
                                it.description(),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    action: Action,
    onClick: () -> Unit,
    onPress: () -> Unit,
    menuState: ActionMenuState,
    dragSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    PressableWithEmphasis {
        var pressed1 by remember { mutableStateOf(false) }
        val borderAlpha =
            animateFloatAsState(
                if (
                    pressed1 ||
                        dragSelected ||
                        (menuState as? ActionMenuState.Closed)?.let { it.action == action } == true
                ) {
                    1f
                } else 0f
            )
        Box(
            modifier =
                modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        if (action.enabled()) MaterialTheme.colorScheme.surfaceContainer
                        else MaterialTheme.colorScheme.surfaceDim
                    )
                    .border(
                        4.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = borderAlpha.value),
                        shape = MaterialTheme.shapes.medium,
                    )
                    .then(
                        if (action.enabled() && menuState !is ActionMenuState.Closed)
                            Modifier.pointerInput(true) {
                                    detectTapGestures(
                                        onPress = { offset ->
                                            onPress()
                                            pressed1 = true
                                            val press = PressInteraction.Press(offset)
                                            interactionSource.emit(press)
                                            tryAwaitRelease()
                                            pressed1 = false
                                            interactionSource.emit(PressInteraction.Release(press))
                                        },
                                        onTap = { onClick() },
                                    )
                                }
                                .pointerHoverIcon(PointerIcon.Hand)
                        else Modifier
                    )
        ) {
            CompositionLocalProvider(
                LocalContentColor provides
                    if (action.enabled()) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(8.dp).pressEmphasis(),
                ) {
                    Icon(iconRes(action.icon()), null)
                    Text(
                        action.name(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}
