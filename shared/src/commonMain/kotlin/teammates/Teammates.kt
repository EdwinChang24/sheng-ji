package teammates

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun Teammates(
    editing: Boolean,
    savedTeammatesRad: Map<String, Float>,
    setSavedTeammatesRad: (Map<String, Float>) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .zIndex(-3f)
                    .alpha(animateFloatAsState(if (editing) 0.75f else 0f).value)
                    .background(MaterialTheme.colorScheme.surface)
                    .then(if (editing) Modifier.pointerInput(true) {} else Modifier)
        )
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        val mainButtonRadiusPx = mainButtonRadiusPx
        var teammateOffsetsKey by rememberSaveable { mutableIntStateOf(0) }
        val teammateOffsets =
            remember(teammateOffsetsKey) {
                mutableStateMapOf(
                    *savedTeammatesRad
                        .map { (id, angleRad) ->
                            id to
                                calculateRestingOffset(
                                    angleRad,
                                    (width / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx,
                                    (height / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx
                                )
                        }
                        .toTypedArray()
                )
            }
        LaunchedEffect(teammateOffsets.toMap()) {
            setSavedTeammatesRad(teammateOffsets.mapValues { (_, offset) -> offset.atan2() })
        }
        val getRestingOffset = { offset: Offset ->
            calculateRestingOffset(
                angleRadians = offset.atan2(),
                (width / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx,
                (height / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx
            )
        }
        val dragging = remember {
            mutableStateMapOf(*savedTeammatesRad.map { (id, _) -> id to false }.toTypedArray())
        }
        var draggingNew by remember { mutableStateOf(false) }
        LaunchedEffect(savedTeammatesRad) {
            if (dragging.none { it.value } && !draggingNew) {
                teammateOffsetsKey++
            }
        }
        TeammatesMainButton(
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
                    getRestingOffset = getRestingOffset,
                    draggingOthers = dragging.any { id != it.key && it.value } || draggingNew,
                    setDragging = { dragging[id] = it },
                    delete = { teammateOffsets.remove(id) }
                )
            }
        }
        var recentlyCleared: Map<String, Float>? by rememberSaveable { mutableStateOf(null) }
        LaunchedEffect(teammateOffsets.toMap()) {
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
                                (height / 2 - mainButtonRadiusPx) / 2 + mainButtonRadiusPx
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
            modifier = Modifier.align(Alignment.Center).zIndex(2f)
        ) {
            if (dragging.none { it.value } && !draggingNew) {
                TeammatesActionButtons(
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
