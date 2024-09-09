package util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ParentDataModifierNode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

open class ExpandWidthsScope(val expandWidths: Boolean) {
    fun Modifier.expandWidth() = if (expandWidths) fillMaxWidth() else this

    data object Weight

    class WeightNode : ParentDataModifierNode, Modifier.Node() {
        override fun Density.modifyParentData(parentData: Any?) = Weight
    }

    class WeightElement : ModifierNodeElement<WeightNode>() {
        override fun create() = WeightNode()

        override fun update(node: WeightNode) {}

        override fun equals(other: Any?) = other is WeightElement

        override fun hashCode() = 0
    }

    class WeightRowScope(expandWidths: Boolean) : ExpandWidthsScope(expandWidths) {
        fun Modifier.weight() = this then WeightElement()
    }
}

@Composable
fun ExpandWidthsScope.WeightRow(
    spacing: Dp = 0.dp,
    modifier: Modifier = Modifier,
    content: @Composable ExpandWidthsScope.WeightRowScope.() -> Unit,
) {
    val density = LocalDensity.current
    val spacingPx = with(density) { spacing.roundToPx() }
    SubcomposeLayout(modifier = modifier) { constraints ->
        if (expandWidths) {
            val measurables = subcompose(0) { ExpandWidthsScope.WeightRowScope(true).content() }
            val widthWithoutWeighted =
                measurables.sumOf {
                    if (it.parentData == ExpandWidthsScope.Weight) 0
                    else
                        it.measure(constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth))
                            .width
                } + spacingPx * (measurables.size - 1)
            val weightedWidth =
                ((constraints.maxWidth - widthWithoutWeighted) /
                        (measurables
                            .count { it.parentData == ExpandWidthsScope.Weight }
                            .takeIf { it != 0 } ?: 1))
                    .coerceAtLeast(0)
            val finalMeasurables =
                subcompose(1) { ExpandWidthsScope.WeightRowScope(true).content() }
            val finalPlaceables = mutableMapOf<Int, Placeable>()
            var x = 0
            for (measurable in finalMeasurables) {
                val placeable =
                    measurable.measure(
                        if (measurable.parentData == ExpandWidthsScope.Weight)
                            constraints.copy(minWidth = weightedWidth, maxWidth = weightedWidth)
                        else
                            constraints.copy(
                                minWidth = 0,
                                maxWidth = (constraints.maxWidth - x).coerceAtLeast(0),
                            )
                    )
                finalPlaceables[x] = placeable
                x += placeable.width + spacingPx
            }
            val height = finalPlaceables.values.maxOf { it.height }
            layout(width = constraints.maxWidth, height = height) {
                finalPlaceables.forEach { (position, placeable) ->
                    placeable.place(position, (height - placeable.height) / 2)
                }
            }
        } else {
            val measurables = subcompose(0) { ExpandWidthsScope.WeightRowScope(false).content() }
            val placeables =
                measurables.map {
                    it.measure(constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth))
                }
            val weightedWidth =
                placeables.maxOf { if (it.parentData == ExpandWidthsScope.Weight) it.width else 0 }
            val totalWidth =
                placeables.sumOf {
                    if (it.parentData == ExpandWidthsScope.Weight) weightedWidth else it.width
                } + spacingPx * (placeables.size - 1)
            if (totalWidth > constraints.maxWidth) {
                val weightedCount = placeables.count { it.parentData == ExpandWidthsScope.Weight }
                val newWeightedWidth =
                    ((constraints.maxWidth - (totalWidth - weightedWidth * weightedCount)) /
                            (weightedCount.takeIf { it != 0 } ?: 1))
                        .coerceAtLeast(0)
                val finalMeasurables =
                    subcompose(1) { ExpandWidthsScope.WeightRowScope(false).content() }
                var maxHeight = 0
                var x = 0
                for (measurable in finalMeasurables) {
                    val placeable =
                        measurable.measure(
                            if (measurable.parentData == ExpandWidthsScope.Weight)
                                constraints.copy(
                                    minWidth = newWeightedWidth,
                                    maxWidth = newWeightedWidth,
                                )
                            else
                                constraints.copy(
                                    minWidth = 0,
                                    maxWidth = (constraints.maxWidth - x).coerceAtLeast(0),
                                )
                        )
                    maxHeight = maxOf(placeable.height, maxHeight)
                    x += placeable.width + spacingPx
                    if (x >= constraints.maxWidth) break
                }
                layout(width = constraints.maxWidth, height = maxHeight) {}
            } else {
                val finalPlaceables =
                    subcompose(1) { ExpandWidthsScope.WeightRowScope(false).content() }
                        .map {
                            it.measure(
                                if (it.parentData == ExpandWidthsScope.Weight)
                                    constraints.copy(
                                        minWidth = weightedWidth,
                                        maxWidth = weightedWidth,
                                    )
                                else constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth)
                            )
                        }
                layout(
                    width =
                        finalPlaceables.sumOf { it.width } + spacingPx * (finalPlaceables.size - 1),
                    height = finalPlaceables.maxOf { it.height },
                ) {}
            }
        }
    }
}

@Composable
fun ExpandWidths(modifier: Modifier = Modifier, content: @Composable ExpandWidthsScope.() -> Unit) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val width =
            subcompose(0) { ExpandWidthsScope(expandWidths = false).content() }
                .maxOf { it.measure(constraints).width }
        val final =
            subcompose(1) { ExpandWidthsScope(expandWidths = true).content() }
                .map { it.measure(constraints.copy(minWidth = width, maxWidth = width)) }
        layout(width = width, height = final.maxOf { it.height }) {
            final.forEach { it.place(0, 0) }
        }
    }
}
