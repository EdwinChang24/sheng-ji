package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.edwinchang24.shengjidisplay.util.allRanks
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PossibleTrumps(possibleTrumps: Set<String>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val width = with(LocalDensity.current) { constraints.maxWidth.toFloat().toDp() }
        val height = with(LocalDensity.current) { constraints.maxHeight.toFloat().toDp() }
        val radius = minOf(width, height) / 2.75f
        val infiniteTransition = rememberInfiniteTransition()
        LaunchedEffect(true) { println(radius) }
        val rotation by
            infiniteTransition.animateFloat(
                0f,
                2f * PI.toFloat(),
                animationSpec = infiniteRepeatable(animation = tween(7500, easing = LinearEasing))
            )
        possibleTrumps.forEach { possibleTrump ->
            val angle =
                allRanks.indexOf(possibleTrump).toFloat() / allRanks.size * 2 * PI.toFloat() +
                    rotation
            Layout(
                content = {
                    Text(
                        possibleTrump,
                        style =
                            LocalTextStyle.current.copy(
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold
                            )
                    )
                },
                modifier =
                    Modifier.absoluteOffset(width / 2, height / 2)
                        .absoluteOffset(radius * sin(angle), radius * -cos(angle))
            ) { measurables, constraints ->
                val placeable = measurables.first().measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(-placeable.width / 2, -placeable.height / 2)
                }
            }
        }
    }
}
