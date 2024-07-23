package teammates

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import resources.Res
import resources.ic_drag_pan
import util.iconRes

@Composable
fun BoxWithConstraintsScope.Teammate(
    editing: Boolean,
    offset: Offset,
    onOffsetChange: (Offset) -> Unit,
    getRestingOffset: (Offset) -> Offset,
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
    LaunchedEffect(offset) { if (!isDragging) offsetLocal = offset }
    LaunchedEffect(isDragging) {
        if (!isDragging) {
            setDragging(false)
            if (new) {
                if (offset.getDistanceSquared() > mainButtonRadiusPx.pow(2)) {
                    onOffsetChange(getRestingOffset(offset))
                } else delete()
            } else {
                if (offsetLocal.getDistanceSquared() > mainButtonRadiusPx.pow(2)) {
                    offsetLocal = getRestingOffset(offsetLocal)
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
                getRestingOffset(calcOffset) -
                    Offset((cos(it) * arrowLength).toFloat(), (sin(it) * arrowLength).toFloat())
            }
        val midArrow2 =
            (calcOffset.atan2() - PI / 4).let {
                getRestingOffset(calcOffset) -
                    Offset((cos(it) * arrowLength).toFloat(), (sin(it) * arrowLength).toFloat())
            }
        val color =
            if (draggingOthers && !isDragging) MaterialTheme.colorScheme.inversePrimary
            else MaterialTheme.colorScheme.primary
        val fadeColor by animateColorAsState(if (editing) color else Color.Transparent)
        Canvas(modifier = Modifier.fillMaxSize().zIndex(if (isDragging) -1f else -2f).alpha(1f)) {
            translate(left = width / 2, top = height / 2) {
                drawLine(
                    Brush.Companion.radialGradient(
                        0.05f to fadeColor,
                        0.5f to color,
                        center = Offset.Zero
                    ),
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
                    start = getRestingOffset(calcOffset),
                    end = midArrow1,
                    strokeWidth = strokeRadius * 2,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color,
                    start = getRestingOffset(calcOffset),
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
                                .pointerHoverIcon(PointerIcon.Hand)
                        else Modifier
                    )
        ) {
            Icon(
                iconRes(Res.drawable.ic_drag_pan),
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
