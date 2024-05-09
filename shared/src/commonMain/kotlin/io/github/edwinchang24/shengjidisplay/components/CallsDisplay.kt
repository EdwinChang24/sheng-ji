package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.Call

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CallsDisplay(
    calls: List<Call>,
    setFound: (index: Int, found: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        calls.forEachIndexed { index, call ->
            PressableWithEmphasis {
                OutlinedCard(modifier = Modifier.size(128.dp)) {
                    Box(
                        modifier =
                            Modifier.fillMaxSize()
                                .clickableForEmphasis(
                                    onLongClick = {
                                        setFound(
                                            index,
                                            (call.found + call.number) % (call.number + 1)
                                        )
                                    },
                                    onClick = {
                                        setFound(index, (call.found + 1) % (call.number + 1))
                                    }
                                )
                    ) {
                        Column(
                            verticalArrangement =
                                Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier =
                                Modifier.fillMaxSize()
                                    .alpha(
                                        animateFloatAsState(
                                                targetValue =
                                                    if (call.found == call.number) 0.5f else 1f,
                                                label = ""
                                            )
                                            .value
                                    )
                                    .padding(16.dp)
                                    .pressEmphasis()
                        ) {
                            PlayingCard(
                                call.card,
                                textStyle = LocalTextStyle.current.copy(fontSize = 48.sp)
                            )
                            CallFoundText(
                                call = call,
                                style = LocalTextStyle.current.copy(fontSize = 28.sp)
                            )
                        }
                        val primaryColor = MaterialTheme.colorScheme.primary
                        val lineScale by
                            animateFloatAsState(
                                if (call.found == call.number) 0.5f else 0f,
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
}
