package components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import interaction.PressableWithEmphasis
import resources.Res
import resources.ic_add
import resources.ic_remove
import util.ExpandHeights
import util.ExpandWidths
import util.WeightRow
import util.iconRes

@Composable
fun NumberPicker(
    value: Int,
    setValue: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 1..Int.MAX_VALUE
) {
    LaunchedEffect(value) { if (value !in range) setValue(value.coerceIn(range)) }
    ExpandWidths {
        ExpandHeights(modifier = modifier) {
            WeightRow(spacing = 16.dp) {
                PressableWithEmphasis {
                    OutlinedCard(
                        enabled = value - 1 in range,
                        onClick = { if (value - 1 in range) setValue(value - 1) },
                        interactionSource = interactionSource,
                        modifier = Modifier.expandHeight().weight()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.expandHeight().expandWidth()
                        ) {
                            Icon(
                                iconRes(Res.drawable.ic_remove),
                                null,
                                modifier = Modifier.pressEmphasis()
                            )
                        }
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.expandHeight().weight()
                ) {
                    AnimatedContent(
                        targetState = value,
                        transitionSpec = {
                            if (targetState > initialState) {
                                    fadeIn() + slideInVertically { -it } togetherWith
                                        fadeOut() + slideOutVertically { it }
                                } else {
                                    fadeIn() + slideInVertically { it } togetherWith
                                        fadeOut() + slideOutVertically { -it }
                                }
                                .using(SizeTransform(clip = false))
                        },
                        label = ""
                    ) { target ->
                        Text(target.toString())
                    }
                }
                PressableWithEmphasis {
                    OutlinedCard(
                        enabled = value + 1 in range,
                        onClick = { if (value + 1 in range) setValue(value + 1) },
                        interactionSource = interactionSource,
                        modifier =
                            Modifier.expandHeight().weight().pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.expandHeight().expandWidth()
                        ) {
                            Icon(
                                iconRes(Res.drawable.ic_add),
                                null,
                                modifier = Modifier.pressEmphasis()
                            )
                        }
                    }
                }
            }
        }
    }
}
