package io.github.edwinchang24.shengjidisplay.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
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
                    constraints: Constraints
                ): MeasureResult {
                    val placeable =
                        measurable.measure(
                            Constraints(
                                minWidth = constraints.minHeight,
                                maxWidth = constraints.maxHeight,
                                minHeight = constraints.minWidth,
                                maxHeight = constraints.maxWidth
                            )
                        )
                    return layout(width = placeable.height, height = placeable.width) {
                        placeable.place(
                            x = -(placeable.width - placeable.height) / 2,
                            y = (placeable.width - placeable.height) / 2
                        )
                    }
                }

                override fun IntrinsicMeasureScope.maxIntrinsicHeight(
                    measurable: IntrinsicMeasurable,
                    width: Int
                ) = measurable.maxIntrinsicWidth(height = width)

                override fun IntrinsicMeasureScope.maxIntrinsicWidth(
                    measurable: IntrinsicMeasurable,
                    height: Int
                ) = measurable.maxIntrinsicHeight(width = height)

                override fun IntrinsicMeasureScope.minIntrinsicHeight(
                    measurable: IntrinsicMeasurable,
                    width: Int
                ) = measurable.minIntrinsicWidth(height = width)

                override fun IntrinsicMeasureScope.minIntrinsicWidth(
                    measurable: IntrinsicMeasurable,
                    height: Int
                ) = measurable.minIntrinsicHeight(width = height)
            }
            .rotate(if (negative) -90f else 90f)
