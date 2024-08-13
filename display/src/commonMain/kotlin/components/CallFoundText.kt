package components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Call

@Composable
fun CallFoundText(
    call: Call,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    val spacing = with(LocalDensity.current) { 4.dp.roundToPx() }
    Layout(
        content = {
            AnimatedContent(
                targetState = call.found,
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
                Text(
                    target.toString(),
                    style = style.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            }
            Text("/", style = style, maxLines = 1, overflow = TextOverflow.Visible)
            AnimatedContent(
                targetState = call.number,
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
                Text(
                    target.toString(),
                    style = style.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val itemWidth = placeables.maxOf { it.width }
        layout(
            width = itemWidth * placeables.size + spacing * (placeables.size - 1),
            height = placeables.maxOf { it.height }
        ) {
            var x = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x + (itemWidth - placeable.width) / 2, 0)
                x += placeables.maxOf { it.width } + spacing
            }
        }
    }
}
