package display.content

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import interaction.PressableWithEmphasis
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import model.AppState
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_add
import util.allRanks
import util.iconRes

@Composable
fun AnimatedContentScope.PossibleTrumpsDisplay(
    state: AppState.Prop,
    navigator: Navigator,
    displayScale: Float,
    modifier: Modifier = Modifier
) {
    val scale by
        transition.animateFloat({ tween(1000) }) { s ->
            when (s) {
                EnterExitState.PreEnter -> 0.5f
                EnterExitState.Visible -> 1f
                EnterExitState.PostExit -> 0.5f
            }
        }
    PressableWithEmphasis {
        if (state().possibleTrumps.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    modifier
                        .clip(MaterialTheme.shapes.large)
                        .clickableForEmphasis(
                            onClick = { navigator.navigate(Dialog.EditPossibleTrumps) }
                        )
                        .padding(24.dp)
                        .pressEmphasis()
            ) {
                Text("No possible trumps added")
                OutlinedButton(onClick = { navigator.navigate(Dialog.EditPossibleTrumps) }) {
                    Icon(iconRes(Res.drawable.ic_add), null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add")
                }
            }
        } else {
            PossibleTrumps(
                state,
                displayScale = displayScale,
                modifier =
                    modifier
                        .size(300.dp * scale * displayScale)
                        .clip(MaterialTheme.shapes.large)
                        .then(
                            if (state().settings.possibleTrumpsDisplay.tapToEdit)
                                Modifier.clickableForEmphasis(
                                    onClick = { navigator.navigate(Dialog.EditPossibleTrumps) }
                                )
                            else Modifier
                        )
                        .pressEmphasis()
            )
        }
    }
}

@Composable
private fun PossibleTrumps(
    state: AppState.Prop,
    displayScale: Float,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val width = with(LocalDensity.current) { constraints.maxWidth.toFloat().toDp() }
        val height = with(LocalDensity.current) { constraints.maxHeight.toFloat().toDp() }
        val radius = minOf(width, height) / 2.75f
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by
            infiniteTransition.animateFloat(
                0f,
                2f * PI.toFloat(),
                animationSpec = infiniteRepeatable(animation = tween(7500, easing = LinearEasing))
            )
        state().possibleTrumps.forEach { possibleTrump ->
            val angle =
                allRanks.indexOf(possibleTrump).toFloat() / allRanks.size * 2 * PI.toFloat() +
                    rotation
            Layout(
                content = {
                    var baseline: Float? by rememberSaveable { mutableStateOf(null) }
                    val underlineLength by
                        animateFloatAsState(
                            if (
                                state().settings.general.underline6And9 &&
                                    possibleTrump in setOf("6", "9")
                            )
                                1f
                            else 0f
                        )
                    val color = LocalContentColor.current
                    Text(
                        possibleTrump,
                        style =
                            LocalTextStyle.current.copy(
                                fontSize = 56.sp * displayScale,
                                fontWeight = FontWeight.Bold
                            ),
                        onTextLayout = { baseline = it.lastBaseline },
                        modifier =
                            Modifier.drawBehind {
                                if (underlineLength > 0f) {
                                    baseline?.let { bl ->
                                        drawLine(
                                            color,
                                            start =
                                                Offset(
                                                    size.width / 2 -
                                                        (size.width / 4 * underlineLength),
                                                    bl + size.height / 8
                                                ),
                                            end =
                                                Offset(
                                                    size.width / 2 +
                                                        (size.width / 4 * underlineLength),
                                                    bl + size.height / 8
                                                )
                                        )
                                    }
                                }
                            }
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
