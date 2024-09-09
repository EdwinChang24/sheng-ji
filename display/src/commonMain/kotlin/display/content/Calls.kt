package display.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.optics.get
import components.CallFoundText
import components.PlayingCard
import interaction.PressableWithEmphasis
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import model.AppState
import model.Call
import model.calls
import model.found
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_add
import util.DefaultTransition
import util.iconRes

const val MaxItemsPerRow = 2

@OptIn(ExperimentalUuidApi::class)
@Composable
fun CallsDisplay(
    state: AppState.Prop,
    navigator: Navigator,
    displayScale: Float,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state().calls,
        transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
        contentKey = { it.isEmpty() },
        modifier = modifier,
    ) { targetCalls ->
        targetCalls
            .takeIf { it.isNotEmpty() }
            ?.let {
                CallsLayout(
                    calls = state().calls,
                    setFound = { index, found -> state { AppState.calls[index].found set found } },
                    state = state,
                    displayScale = displayScale,
                )
            }
            ?: PressableWithEmphasis {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier.clip(MaterialTheme.shapes.large)
                            .clickableForEmphasis(
                                onClick = {
                                    navigator.navigate(Dialog.EditCall(Uuid.random().toString()))
                                }
                            )
                            .padding(24.dp)
                            .pressEmphasis(),
                ) {
                    Text("No calls added")
                    OutlinedButton(
                        onClick = { navigator.navigate(Dialog.EditCall(Uuid.random().toString())) }
                    ) {
                        Icon(iconRes(Res.drawable.ic_add), null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add")
                    }
                }
            }
    }
}

@Composable
private fun CallsCard(
    index: Int,
    call: Call,
    setFound: (index: Int, found: Int) -> Unit,
    state: AppState.Prop,
    displayScale: Float,
) {
    val color = MaterialTheme.colorScheme.outlineVariant
    val lineScale by
        animateFloatAsState(
            if (call.found == call.number) 0.5f else 0f,
            animationSpec = tween(durationMillis = 100),
        )
    val strokeWidth = with(LocalDensity.current) { 4.dp.toPx() * displayScale }
    PressableWithEmphasis {
        OutlinedCard(
            modifier =
                Modifier.clip(CardDefaults.outlinedShape)
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color,
                            start =
                                Offset(
                                    size.width * (0.5f - lineScale),
                                    size.height * (0.5f + lineScale),
                                ),
                            end =
                                Offset(
                                    size.width * (0.5f + lineScale),
                                    size.height * (0.5f - lineScale),
                                ),
                            strokeWidth = strokeWidth,
                        )
                    }
                    .clickableForEmphasis(
                        onLongClick = {
                            setFound(index, (call.found + call.number) % (call.number + 1))
                        },
                        onClick = { setFound(index, (call.found + 1) % (call.number + 1)) },
                    )
        ) {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(4.dp * displayScale, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier.align(Alignment.CenterHorizontally)
                        .alpha(
                            animateFloatAsState(
                                    targetValue = if (call.found == call.number) 0.4f else 1f,
                                    label = "",
                                )
                                .value
                        )
                        .padding(16.dp * displayScale)
                        .pressEmphasis(),
            ) {
                PlayingCard(
                    call.card,
                    state = state,
                    textStyle = LocalTextStyle.current.copy(fontSize = 48.sp * displayScale),
                )
                CallFoundText(
                    call = call,
                    style = LocalTextStyle.current.copy(fontSize = 28.sp * displayScale),
                )
            }
        }
    }
}

@Composable
private fun CallsLayout(
    calls: List<Call>,
    setFound: (index: Int, found: Int) -> Unit,
    state: AppState.Prop,
    displayScale: Float,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp * displayScale),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        calls.chunked(MaxItemsPerRow).forEachIndexed { rowIndex, rowCalls ->
            SubcomposeLayout { constraints ->
                val content =
                    @Composable {
                        rowCalls.forEachIndexed { index, call ->
                            CallsCard(
                                rowIndex * MaxItemsPerRow + index,
                                call,
                                setFound,
                                state,
                                displayScale,
                            )
                        }
                    }
                val placeables = subcompose(0, content).map { it.measure(constraints) }
                val height = placeables.maxOfOrNull { it.height } ?: 0
                val placeablesFinal =
                    subcompose(1, content).map { m ->
                        m.measure(constraints.copy(minHeight = height, maxHeight = height))
                    }
                layout(
                    width =
                        placeables.sumOf { it.width } +
                            (MaxItemsPerRow * 8.dp.toPx() * displayScale).roundToInt(),
                    height = placeables.maxOfOrNull { it.height } ?: 0,
                ) {
                    var x = 0
                    placeablesFinal.forEachIndexed { index, placeable ->
                        if (index != 0) x += (8.dp.toPx() * displayScale).roundToInt()
                        placeable.place(x, 0)
                        x += placeable.width
                    }
                }
            }
        }
    }
}
