package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.model.PlayingCard
import io.github.edwinchang24.shengjidisplay.model.Suit
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme
import io.github.edwinchang24.shengjidisplay.util.formatCallNumber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CallsDisplay(
    calls: List<Call>,
    setFound: (index: Int, found: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        calls.forEachIndexed { index, call ->
            val interactionSource = remember { MutableInteractionSource() }
            OutlinedCard(
                onClick = { setFound(index, !call.found) },
                interactionSource = interactionSource,
                modifier = Modifier.weight(1f).height(IntrinsicSize.Max)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val scale = remember { Animatable(1f) }
                    LaunchedEffect(true) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Press) {
                                scale.snapTo(0.75f)
                            } else if (
                                it is PressInteraction.Cancel || it is PressInteraction.Release
                            ) {
                                scale.animateTo(1f)
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier =
                            Modifier.padding(16.dp)
                                .alpha(
                                    animateFloatAsState(if (call.found) 0.5f else 1f, label = "")
                                        .value
                                )
                                .scale(scale.value)
                    ) {
                        PlayingCard(
                            call.card,
                            textStyle = LocalTextStyle.current.copy(fontSize = 40.sp)
                        )
                        Text(formatCallNumber(call.number), fontSize = 32.sp)
                    }
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val lineScale by
                        animateFloatAsState(
                            if (call.found) 0.5f else 0f,
                            animationSpec = tween(durationMillis = 100),
                            label = ""
                        )
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            primaryColor,
                            start =
                                Offset(
                                    size.width * (0.5f - lineScale),
                                    size.height * (0.5f + lineScale)
                                ),
                            end =
                                Offset(
                                    size.width * (0.5f + lineScale),
                                    size.height * (0.5f - lineScale)
                                ),
                            strokeWidth = 16f
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CallsDisplayPreview() {
    ShengJiDisplayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var calls by remember {
                mutableStateOf(
                    listOf(
                        Call(PlayingCard("A", Suit.SPADES), number = 1, found = false),
                        Call(PlayingCard("K", Suit.HEARTS), number = 2, found = true)
                    )
                )
            }
            CallsDisplay(
                calls,
                { index, found ->
                    calls = calls.toMutableList().also { it[index] = it[index].copy(found = found) }
                }
            )
        }
    }
}
