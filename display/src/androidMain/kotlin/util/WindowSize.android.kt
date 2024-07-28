package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.window.layout.WindowMetricsCalculator

@Composable
actual fun calculateWindowWidth(): WindowWidth {
    LocalConfiguration.current
    val metrics =
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(LocalContext.current)
    val size = with(LocalDensity.current) { metrics.bounds.toComposeRect().size.toDpSize() }
    return WindowWidth.from(size)
}

@Composable
actual fun calculateWindowHeight(): WindowHeight {
    LocalConfiguration.current
    val metrics =
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(LocalContext.current)
    val size = with(LocalDensity.current) { metrics.bounds.toComposeRect().size.toDpSize() }
    return WindowHeight.from(size)
}
