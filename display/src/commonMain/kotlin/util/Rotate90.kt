package util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints

fun Modifier.rotate90(negative: Boolean = false) =
    this then
        object : LayoutModifier {
                override fun MeasureScope.measure(
                    measurable: Measurable,
                    constraints: Constraints,
                ): MeasureResult {
                    val placeable =
                        measurable.measure(
                            Constraints(
                                minWidth = constraints.minHeight,
                                maxWidth = constraints.maxHeight,
                                minHeight = constraints.minWidth,
                                maxHeight = constraints.maxWidth,
                            )
                        )
                    return layout(width = placeable.height, height = placeable.width) {
                        placeable.place(
                            x = -(placeable.width - placeable.height) / 2,
                            y = (placeable.width - placeable.height) / 2,
                        )
                    }
                }
            }
            .rotate(if (negative) -90f else 90f)
