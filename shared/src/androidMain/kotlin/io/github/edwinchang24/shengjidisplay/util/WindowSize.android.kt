package io.github.edwinchang24.shengjidisplay.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.window.layout.WindowMetricsCalculator

@Composable
actual fun calculateWindowSize(): WindowSize {
    LocalConfiguration.current
    val metrics =
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(LocalContext.current)
    val size = with(LocalDensity.current) { metrics.bounds.toComposeRect().size.toDpSize() }
    return WindowSize.from(size)
}
