package util

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ParentDataModifierNode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

open class ExpandHeightsScope(val expandHeights: Boolean) {
    fun Modifier.expandHeight() = if (expandHeights) fillMaxHeight() else this

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

    class WeightColumnScope(expandHeights: Boolean) : ExpandHeightsScope(expandHeights) {
        fun Modifier.weight() = this then WeightElement()
    }
}

@Composable
fun ExpandHeightsScope.WeightColumn(
    spacing: Dp = 0.dp,
    modifier: Modifier = Modifier,
    content: @Composable ExpandHeightsScope.WeightColumnScope.() -> Unit
) {
    val density = LocalDensity.current
    val spacingPx = with(density) { spacing.roundToPx() }
    SubcomposeLayout(modifier = modifier) { constraints ->
        if (expandHeights) {
            val measurables = subcompose(0) { ExpandHeightsScope.WeightColumnScope(true).content() }
            val heightWithoutWeighted =
                measurables.sumOf {
                    if (it.parentData == ExpandHeightsScope.Weight) 0
                    else
                        it.measure(
                                constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)
                            )
                            .height
                } + spacingPx * (measurables.size - 1)
            val weightedHeight =
                ((constraints.maxHeight - heightWithoutWeighted) /
                        measurables.count { it.parentData == ExpandHeightsScope.Weight })
                    .coerceAtLeast(0)
            val finalMeasurables =
                subcompose(1) { ExpandHeightsScope.WeightColumnScope(true).content() }
            val finalPlaceables = mutableMapOf<Int, Placeable>()
            var y = 0
            for (measurable in finalMeasurables) {
                val placeable =
                    measurable.measure(
                        if (measurable.parentData == ExpandHeightsScope.Weight)
                            constraints.copy(minHeight = weightedHeight, maxHeight = weightedHeight)
                        else
                            constraints.copy(
                                minHeight = 0,
                                maxHeight = (constraints.maxHeight - y).coerceAtLeast(0)
                            )
                    )
                finalPlaceables[y] = placeable
                y += placeable.height + spacingPx
            }
            layout(
                width = finalPlaceables.values.maxOf { it.width },
                height = constraints.maxHeight
            ) {
                finalPlaceables.forEach { (position, placeable) -> placeable.place(0, position) }
            }
        } else {
            val measurables =
                subcompose(0) { ExpandHeightsScope.WeightColumnScope(false).content() }
            val placeables =
                measurables.map {
                    it.measure(constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity))
                }
            val weightedHeight =
                placeables.filter { it.parentData == ExpandHeightsScope.Weight }.maxOf { it.height }
            val totalHeight =
                placeables.sumOf {
                    if (it.parentData == ExpandHeightsScope.Weight) weightedHeight else it.height
                } + spacingPx * (placeables.size - 1)
            if (totalHeight > constraints.maxHeight) {
                val weightedCount = placeables.count { it.parentData == ExpandHeightsScope.Weight }
                val newWeightedHeight =
                    ((constraints.maxHeight - (totalHeight - weightedHeight * weightedCount)) /
                            weightedCount)
                        .coerceAtLeast(0)
                val finalMeasurables =
                    subcompose(1) { ExpandHeightsScope.WeightColumnScope(false).content() }
                val finalPlaceables = mutableMapOf<Int, Placeable>()
                var y = 0
                for (measurable in finalMeasurables) {
                    val placeable =
                        measurable.measure(
                            if (measurable.parentData == ExpandHeightsScope.Weight)
                                constraints.copy(
                                    minHeight = newWeightedHeight,
                                    maxHeight = newWeightedHeight
                                )
                            else
                                constraints.copy(
                                    minHeight = 0,
                                    maxHeight = (constraints.maxHeight - y).coerceAtLeast(0)
                                )
                        )
                    finalPlaceables[y] = placeable
                    y += placeable.height + spacingPx
                }
                layout(
                    width = finalPlaceables.values.maxOf { it.width },
                    height = constraints.maxHeight
                ) {
                    finalPlaceables.forEach { (position, placeable) ->
                        placeable.place(0, position)
                    }
                }
            } else {
                val finalPlaceables =
                    subcompose(1) { ExpandHeightsScope.WeightColumnScope(false).content() }
                        .map {
                            it.measure(
                                if (it.parentData == ExpandHeightsScope.Weight)
                                    constraints.copy(
                                        minHeight = weightedHeight,
                                        maxHeight = weightedHeight
                                    )
                                else
                                    constraints.copy(
                                        minHeight = 0,
                                        maxHeight = Constraints.Infinity
                                    )
                            )
                        }
                layout(
                    width = finalPlaceables.maxOf { it.width },
                    height =
                        finalPlaceables.sumOf { it.height } + spacingPx * (finalPlaceables.size - 1)
                ) {
                    var y = 0
                    finalPlaceables.forEach { placeable ->
                        placeable.place(0, y)
                        y += placeable.height + spacingPx
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandHeights(
    modifier: Modifier = Modifier,
    content: @Composable ExpandHeightsScope.() -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val height =
            subcompose(0) { ExpandHeightsScope(expandHeights = false).content() }
                .maxOf { it.measure(constraints).height }
        val final =
            subcompose(1) { ExpandHeightsScope(expandHeights = true).content() }
                .map { it.measure(constraints.copy(minHeight = height, maxHeight = height)) }
        layout(width = final.maxOf { it.width }, height = height) {
            final.forEach { it.place(0, 0) }
        }
    }
}
