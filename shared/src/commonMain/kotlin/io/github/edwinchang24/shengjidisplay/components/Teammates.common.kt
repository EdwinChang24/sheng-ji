package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import com.benasher44.uuid.uuid4
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_clear_all
import io.github.edwinchang24.shengjidisplay.resources.ic_done
import io.github.edwinchang24.shengjidisplay.resources.ic_drag_pan
import io.github.edwinchang24.shengjidisplay.resources.ic_undo
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import org.jetbrains.compose.resources.painterResource

@Composable
fun Teammates(
    editing: Boolean,
    savedTeammatesRad: Map<String, Float>,
    setSavedTeammatesRad: (Map<String, Float>) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        val mainButtonRadiusPx = mainButtonRadiusPx
        var teammateOffsets = remember {
            mutableStateMapOf(
                *savedTeammatesRad
                    .map { (id, angleRad) ->
                        id to
                            calculateRestingOffset(
                                angleRad,
                                (width / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx,
                                (height / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx,
                            )
                    }
                    .toTypedArray()
            )
        }
        LaunchedEffect(
            *if (teammateOffsets.isNotEmpty()) teammateOffsets.values.toTypedArray()
            else arrayOf(Offset.Unspecified)
        ) {
            setSavedTeammatesRad(teammateOffsets.mapValues { (_, offset) -> offset.atan2() })
        }
        val getRestingOffset = { offset: Offset ->
            calculateRestingOffset(
                angleRadians = offset.atan2(),
                (width / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx,
                (height / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx
            )
        }
        var dragging = remember {
            mutableStateMapOf(*savedTeammatesRad.map { (id, _) -> id to false }.toTypedArray())
        }
        var draggingNew by remember { mutableStateOf(false) }
        MainButton(
            editing = editing,
            dragging = dragging.values.any { it } || draggingNew,
            deletingTeammate =
                teammateOffsets.any { (id, offset) ->
                    dragging.getOrElse(id) { false } &&
                        offset.getDistanceSquared() <= mainButtonRadiusPx.pow(2)
                },
            setDraggingNew = { draggingNew = it },
            getRestingOffset,
            addNewTeammate = { (id, offset) -> teammateOffsets[id] = offset }
        )
        teammateOffsets.forEach { (id, offset) ->
            key(id) {
                Teammate(
                    editing = editing,
                    offset = offset,
                    onOffsetChange = { teammateOffsets[id] = it },
                    getRestingOffest = getRestingOffset,
                    draggingOthers = dragging.any { id != it.key && it.value } || draggingNew,
                    setDragging = { dragging[id] = it },
                    delete = { teammateOffsets.remove(id) }
                )
            }
        }
        var recentlyCleared: Map<String, Float>? by rememberSaveable { mutableStateOf(null) }
        LaunchedEffect(
            *if (teammateOffsets.isNotEmpty()) teammateOffsets.values.toTypedArray()
            else arrayOf(Offset.Unspecified)
        ) {
            if (teammateOffsets.isNotEmpty()) recentlyCleared = null
        }
        val onPressClear = {
            if (teammateOffsets.isNotEmpty()) {
                recentlyCleared = teammateOffsets.mapValues { (_, offset) -> offset.atan2() }
                teammateOffsets.clear()
            } else {
                recentlyCleared?.let {
                    teammateOffsets.putAll(
                        it.mapValues { (_, angleRad) ->
                            calculateRestingOffset(
                                angleRad,
                                (width / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx,
                                (height / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx,
                            )
                        }
                    )
                    recentlyCleared = null
                }
            }
            Unit
        }
        AnimatedVisibility(
            visible = editing,
            enter = fadeIn() + scaleIn(spring(stiffness = Spring.StiffnessHigh)),
            exit = fadeOut() + scaleOut(spring(stiffness = Spring.StiffnessHigh)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (dragging.none { it.value } && !draggingNew) {
                ActionButtons(
                    hasRecentlyCleared = recentlyCleared != null,
                    canClear = teammateOffsets.isNotEmpty(),
                    onPressClear = onPressClear,
                    onDone = onDone
                )
            }
        }
    }
    TeammatesBackHandler(editing) { onDone() }
}

@Composable expect fun TeammatesBackHandler(enabled: Boolean = true, onBack: () -> Unit)

val mainButtonRadiusDp = 48.dp

val mainButtonRadiusPx
    @Composable get() = with(LocalDensity.current) { mainButtonRadiusDp.roundToPx() }.toFloat()

fun Offset.atan2() = atan2(y, x)

fun calculateRestingOffset(angleRadians: Float, radiusX: Float, radiusY: Float): Offset {
    val r =
        radiusX * radiusY /
            sqrt((radiusY * cos(angleRadians)).pow(2) + (radiusX * sin(angleRadians)).pow(2))
    return Offset(cos(angleRadians) * r, sin(angleRadians) * r)
}

@Composable
private fun BoxWithConstraintsScope.Teammate(
    editing: Boolean,
    offset: Offset,
    onOffsetChange: (Offset) -> Unit,
    getRestingOffest: (Offset) -> Offset,
    draggingOthers: Boolean,
    setDragging: (Boolean) -> Unit,
    delete: () -> Unit,
    modifier: Modifier = Modifier,
    new: Boolean = false
) {
    val mainButtonRadiusPx = mainButtonRadiusPx
    val width = constraints.maxWidth.toFloat()
    val height = constraints.maxHeight.toFloat()
    var pressed by remember { mutableStateOf(false) }
    val isDragging = pressed || new
    val sizeDp = 48.dp
    val sizePx = with(LocalDensity.current) { sizeDp.roundToPx() }
    var offsetLocal by remember { mutableStateOf(offset) }
    LaunchedEffect(offsetLocal) { if (!new) onOffsetChange(offsetLocal) }
    LaunchedEffect(isDragging) {
        if (!isDragging) {
            setDragging(false)
            if (new) {
                if (offset.getDistanceSquared() > mainButtonRadiusPx.pow(2)) {
                    onOffsetChange(getRestingOffest(offset))
                } else delete()
            } else {
                if (offsetLocal.getDistanceSquared() > mainButtonRadiusPx.pow(2)) {
                    offsetLocal = getRestingOffest(offsetLocal)
                } else delete()
            }
        } else {
            setDragging(true)
        }
    }
    LaunchedEffect(offset) { if (new) offsetLocal = offset }
    val springSpec = spring<Float>(stiffness = Spring.StiffnessLow)
    val calcOffset =
        with(if (new) offset else offsetLocal) {
            Offset(
                animateFloatAsState(x, if (isDragging) snap() else springSpec).value,
                animateFloatAsState(y, if (isDragging) snap() else springSpec).value
            )
        }
    if (calcOffset.getDistanceSquared() > mainButtonRadiusPx.pow(2)) {
        val strokeRadius = with(LocalDensity.current) { 2.dp.roundToPx().toFloat() }
        val xBound = width / 2 - strokeRadius
        val yBound = height / 2 - strokeRadius
        val intersectionTopBottom =
            Offset(
                x = calcOffset.x / calcOffset.y.absoluteValue * yBound,
                y = if (calcOffset.y > 0) yBound else -yBound
            )
        val endpoint =
            if (intersectionTopBottom.x.absoluteValue < xBound) intersectionTopBottom
            else
                Offset(
                    x = if (calcOffset.x > 0) xBound else -xBound,
                    y = calcOffset.y / calcOffset.x.absoluteValue * xBound
                )
        val arrowLength = with(LocalDensity.current) { 8.dp.roundToPx().toFloat() }
        val endArrow1 =
            (calcOffset.atan2() + PI / 4).let {
                endpoint -
                    Offset((cos(it) * arrowLength).toFloat(), (sin(it) * arrowLength).toFloat())
            }
        val endArrow2 =
            (calcOffset.atan2() - PI / 4).let {
                endpoint -
                    Offset((cos(it) * arrowLength).toFloat(), (sin(it) * arrowLength).toFloat())
            }
        val midArrow1 =
            (calcOffset.atan2() + PI / 4).let {
                getRestingOffest(calcOffset) -
                    Offset((cos(it) * arrowLength).toFloat(), (sin(it) * arrowLength).toFloat())
            }
        val midArrow2 =
            (calcOffset.atan2() - PI / 4).let {
                getRestingOffest(calcOffset) -
                    Offset((cos(it) * arrowLength).toFloat(), (sin(it) * arrowLength).toFloat())
            }
        val color =
            if (draggingOthers && !isDragging) MaterialTheme.colorScheme.inversePrimary
            else MaterialTheme.colorScheme.primary
        val fadeColor by animateColorAsState(if (editing) color else Color.Transparent)
        Canvas(modifier = Modifier.fillMaxSize().zIndex(if (isDragging) -1f else -2f).alpha(1f)) {
            translate(left = width / 2, top = height / 2) {
                drawLine(
                    Brush.radialGradient(0.05f to fadeColor, 0.5f to color, center = Offset.Zero),
                    start = Offset.Zero,
                    end = endpoint,
                    strokeWidth = strokeRadius * 2,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color,
                    start = endpoint,
                    end = endArrow1,
                    strokeWidth = strokeRadius * 2,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color,
                    start = endpoint,
                    end = endArrow2,
                    strokeWidth = strokeRadius * 2,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color,
                    start = getRestingOffest(calcOffset),
                    end = midArrow1,
                    strokeWidth = strokeRadius * 2,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color,
                    start = getRestingOffest(calcOffset),
                    end = midArrow2,
                    strokeWidth = strokeRadius * 2,
                    cap = StrokeCap.Round
                )
            }
        }
    }
    if (!draggingOthers || isDragging)
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                modifier
                    .absoluteOffset { Offset(width / 2, height / 2).round() }
                    .absoluteOffset { calcOffset.round() }
                    .absoluteOffset { IntOffset(-sizePx / 2, -sizePx / 2) }
                    .zIndex(1f)
                    .size(sizeDp)
                    .alpha(animateFloatAsState(if (editing) 1f else 0f).value)
                    .scale(animateFloatAsState(if (editing) 1f else 0f).value)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        animateDpAsState(
                                if (isDragging) 5.dp else 3.dp,
                                spring(stiffness = Spring.StiffnessHigh)
                            )
                            .value,
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
                    .then(
                        if (editing)
                            Modifier.pointerInput(true) {
                                    detectDragGestures(
                                        onDragStart = { pressed = true },
                                        onDragEnd = { pressed = false },
                                        onDragCancel = { pressed = false }
                                    ) { change, dragAmount ->
                                        change.consume()
                                        if (new) onOffsetChange(offset + dragAmount)
                                        else offsetLocal += dragAmount
                                    }
                                }
                                .pointerInput(true) {
                                    detectTapGestures(
                                        onPress = { pressed = true },
                                        onTap = { pressed = false }
                                    )
                                }
                        else Modifier
                    )
        ) {
            Icon(
                painterResource(Res.drawable.ic_drag_pan),
                null,
                modifier =
                    Modifier.scale(
                        animateFloatAsState(
                                if (isDragging) 1.1f else 0.9f,
                                spring(stiffness = Spring.StiffnessHigh)
                            )
                            .value
                    )
            )
        }
}

@Composable
private fun BoxWithConstraintsScope.MainButton(
    editing: Boolean,
    dragging: Boolean,
    deletingTeammate: Boolean,
    setDraggingNew: (Boolean) -> Unit,
    getRestingOffest: (Offset) -> Offset,
    addNewTeammate: (Pair<String, Offset>) -> Unit,
    modifier: Modifier = Modifier
) {
    val width = constraints.maxWidth.toFloat()
    val height = constraints.maxHeight.toFloat()
    var new: Pair<String, Offset>? by remember { mutableStateOf(null) }
    val mainButtonRadiusPx = mainButtonRadiusPx
    val onRelease = {
        new?.let { n ->
            val teammate = n
            new = null
            addNewTeammate(teammate)
        }
        setDraggingNew(false)
    }
    val deleting =
        deletingTeammate ||
            new?.second?.getDistanceSquared()?.let { it <= mainButtonRadiusPx.pow(2) } ?: false
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .absoluteOffset { Offset(width / 2, height / 2).round() }
                .absoluteOffset { Offset(-mainButtonRadiusPx, -mainButtonRadiusPx).round() }
                .size(mainButtonRadiusDp * 2)
                .alpha(animateFloatAsState(if (editing) 1f else 0f).value)
                .scale(animateFloatAsState(if (editing) 1f else 0f).value)
                .clip(CircleShape)
                .background(
                    animateColorAsState(
                            if (deleting) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.primaryContainer
                        )
                        .value
                )
                .border(
                    4.dp,
                    animateColorAsState(
                            if (deleting) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                        .value,
                    CircleShape
                )
                .then(
                    if (editing) {
                        Modifier.pointerInput(true) {
                                detectDragGestures(
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        new?.let { new = it.copy(second = it.second + dragAmount) }
                                    },
                                    onDragEnd = onRelease,
                                    onDragCancel = onRelease
                                )
                            }
                            .pointerInput(true) {
                                detectTapGestures(
                                    onPress = {
                                        new =
                                            uuid4().toString() to
                                                it - Offset(mainButtonRadiusPx, mainButtonRadiusPx)
                                        setDraggingNew(true)
                                    },
                                    onTap = { onRelease() }
                                )
                            }
                    } else Modifier
                )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                if (deleting) "Release to delete"
                else if (dragging) "Move here to delete" else "Drag to add",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
    new?.let { (id, offset) ->
        key(id) {
            Teammate(
                editing = true,
                offset,
                { new = id to it },
                getRestingOffest,
                draggingOthers = false,
                setDragging = {},
                delete = {},
                new = true
            )
        }
    }
}

@Composable
fun ActionButtons(
    hasRecentlyCleared: Boolean,
    canClear: Boolean,
    onPressClear: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        OutlinedButtonWithEmphasis(
            text = if (!hasRecentlyCleared) "Clear" else "Undo",
            icon =
                painterResource(
                    if (!hasRecentlyCleared) Res.drawable.ic_clear_all else Res.drawable.ic_undo
                ),
            onClick = onPressClear,
            enabled = canClear || hasRecentlyCleared,
            colors =
                ButtonDefaults.outlinedButtonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.surface)
        )
        Spacer(modifier = Modifier.height(mainButtonRadiusDp * 2 + 16.dp))
        OutlinedButtonWithEmphasis(
            text = "Done",
            icon = painterResource(Res.drawable.ic_done),
            onClick = onDone,
            colors =
                ButtonDefaults.outlinedButtonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.surface)
        )
    }
}
