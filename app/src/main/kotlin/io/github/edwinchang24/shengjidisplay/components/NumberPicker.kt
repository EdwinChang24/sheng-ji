package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NumberPicker(
    value: Int,
    setValue: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 1..10
) {
    LaunchedEffect(value) { if (value !in range) setValue(value.coerceIn(range)) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(IntrinsicSize.Max)
    ) {
        val decreaseIS = remember { MutableInteractionSource() }
        OutlinedCard(
            enabled = value - 1 in range,
            onClick = { if (value - 1 in range) setValue(value - 1) },
            interactionSource = decreaseIS,
            modifier = Modifier.fillMaxHeight().weight(2f)
        ) {
            val scale = remember { Animatable(1f) }
            LaunchedEffect(true) {
                decreaseIS.interactions.collectLatest {
                    if (it is PressInteraction.Press) {
                        scale.snapTo(0.75f)
                    } else if (it is PressInteraction.Cancel || it is PressInteraction.Release) {
                        scale.animateTo(1f)
                    }
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    painterResource(R.drawable.ic_remove),
                    null,
                    modifier = Modifier.scale(animateFloatAsState(scale.value, label = "").value)
                )
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight().weight(1f)) {
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
        val increaseIS = remember { MutableInteractionSource() }
        OutlinedCard(
            enabled = value + 1 in range,
            onClick = { if (value + 1 in range) setValue(value + 1) },
            interactionSource = increaseIS,
            modifier = Modifier.fillMaxHeight().weight(2f)
        ) {
            val scale = remember { Animatable(1f) }
            LaunchedEffect(true) {
                increaseIS.interactions.collectLatest {
                    if (it is PressInteraction.Press) {
                        scale.snapTo(0.75f)
                    } else if (it is PressInteraction.Cancel || it is PressInteraction.Release) {
                        scale.animateTo(1f)
                    }
                }
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    painterResource(R.drawable.ic_add),
                    null,
                    modifier = Modifier.scale(scale.value)
                )
            }
        }
    }
}

@Preview
@Composable
private fun NumberPickerPreview() {
    ShengJiDisplayTheme {
        Surface {
            var num by rememberSaveable { mutableIntStateOf(1) }
            NumberPicker(num, { num = it })
        }
    }
}
