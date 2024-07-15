package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.display.DisplayScheme
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.mainDisplay
import io.github.edwinchang24.shengjidisplay.model.possibleTrumpsDisplay
import io.github.edwinchang24.shengjidisplay.model.scale
import io.github.edwinchang24.shengjidisplay.model.settings
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_done
import io.github.edwinchang24.shengjidisplay.util.iconRes

@Composable
fun Scale(
    state: AppState.Prop,
    displayScheme: DisplayScheme,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var cardPosition by rememberSaveable { mutableFloatStateOf(Float.MAX_VALUE) }
    var cardHeight by rememberSaveable { mutableFloatStateOf(0f) }
    Box(modifier = modifier.fillMaxSize().pointerInput(true) {}) {
        BoxWithConstraints(
            modifier =
                Modifier.align(Alignment.Center)
                    .widthIn(max = 600.dp)
                    .safeDrawingPadding()
                    .padding(24.dp)
        ) {
            Layout(
                content = {
                    ElevatedCard(
                        colors =
                            CardDefaults.elevatedCardColors(
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                        modifier =
                            Modifier.onGloballyPositioned { cardHeight = it.size.height.toFloat() }
                                .draggable(
                                    orientation = Orientation.Vertical,
                                    state =
                                        rememberDraggableState { delta -> cardPosition += delta },
                                    onDragStarted = {
                                        cardPosition =
                                            cardPosition.coerceIn(
                                                0f,
                                                with(density) { maxHeight.toPx() } - cardHeight
                                            )
                                    }
                                )
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(24.dp)
                        ) {
                            Slider(
                                when (displayScheme) {
                                    DisplayScheme.Main -> state().settings.mainDisplay.scale
                                    DisplayScheme.PossibleTrumps ->
                                        state().settings.possibleTrumpsDisplay.scale
                                },
                                {
                                    when (displayScheme) {
                                        DisplayScheme.Main ->
                                            state { AppState.settings.mainDisplay.scale set it }
                                        DisplayScheme.PossibleTrumps ->
                                            state {
                                                AppState.settings.possibleTrumpsDisplay.scale set it
                                            }
                                    }
                                },
                                valueRange = 0.2f..4f,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val schemeName =
                                    when (displayScheme) {
                                        DisplayScheme.Main -> "main"
                                        DisplayScheme.PossibleTrumps -> "possible trumps"
                                    }
                                Text(
                                    buildAnnotatedString {
                                        append("Scaling ")
                                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                            append(schemeName)
                                        }
                                        append(" display")
                                    },
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                                ButtonWithEmphasis(
                                    text = "Done",
                                    icon = iconRes(Res.drawable.ic_done),
                                    onClick = onDone
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) { measurables, constraints ->
                val placeable = measurables.first().measure(constraints.copy(minHeight = 0))
                layout(constraints.maxWidth, constraints.maxHeight) {
                    placeable.place(
                        0,
                        cardPosition
                            .toInt()
                            .coerceIn(0, (constraints.maxHeight - cardHeight).toInt())
                    )
                }
            }
        }
    }
}
