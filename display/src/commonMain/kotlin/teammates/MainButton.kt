package teammates

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlin.math.pow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun BoxWithConstraintsScope.TeammatesMainButton(
    editing: Boolean,
    dragging: Boolean,
    deletingTeammate: Boolean,
    setDraggingNew: (Boolean) -> Unit,
    getRestingOffset: (Offset) -> Offset,
    addNewTeammate: (Pair<String, Offset>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val width = constraints.maxWidth.toFloat()
    val height = constraints.maxHeight.toFloat()
    var new: Pair<String, Offset>? by remember { mutableStateOf(null) }
    val mainButtonRadiusPx = mainButtonRadiusPx
    val onRelease = {
        if (new?.second?.getDistanceSquared()?.let { it <= mainButtonRadiusPx.pow(2) } == true) {
            new = null
        }
        new?.let { n ->
            new = null
            addNewTeammate(n)
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
                    CircleShape,
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
                                    onDragCancel = onRelease,
                                )
                            }
                            .pointerInput(true) {
                                detectTapGestures(
                                    onPress = {
                                        new =
                                            Uuid.random().toString() to
                                                it - Offset(mainButtonRadiusPx, mainButtonRadiusPx)
                                        setDraggingNew(true)
                                    },
                                    onTap = { onRelease() },
                                )
                            }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else Modifier
                ),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                if (deleting) "Release to delete"
                else if (dragging) "Move here to delete" else "Drag to add",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
    new?.let { (id, offset) ->
        key(id) {
            Teammate(
                editing = true,
                offset,
                { new = id to offset + it },
                { new = id to it },
                getRestingOffset,
                draggingOthers = false,
                setDragging = {},
                delete = {},
                new = true,
            )
        }
    }
}
