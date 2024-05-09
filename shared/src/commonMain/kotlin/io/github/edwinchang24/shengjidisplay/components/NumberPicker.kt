package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_add
import io.github.edwinchang24.shengjidisplay.resources.ic_remove
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun NumberPicker(
    value: Int,
    setValue: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 1..Int.MAX_VALUE
) {
    LaunchedEffect(value) { if (value !in range) setValue(value.coerceIn(range)) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(IntrinsicSize.Max)
    ) {
        PressableWithEmphasis {
            OutlinedCard(
                enabled = value - 1 in range,
                onClick = { if (value - 1 in range) setValue(value - 1) },
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxHeight().weight(2f)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        painterResource(Res.drawable.ic_remove),
                        null,
                        modifier = Modifier.pressEmphasis()
                    )
                }
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
        PressableWithEmphasis {
            OutlinedCard(
                enabled = value + 1 in range,
                onClick = { if (value + 1 in range) setValue(value + 1) },
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxHeight().weight(2f)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        painterResource(Res.drawable.ic_add),
                        null,
                        modifier = Modifier.pressEmphasis()
                    )
                }
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
