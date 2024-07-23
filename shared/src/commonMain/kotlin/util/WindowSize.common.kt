package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

sealed interface WindowSize {

    companion object {
        fun from(size: DpSize) =
            listOf(Large, Medium, Small).firstOrNull { size.width >= it.breakpoint } ?: Small
    }

    val breakpoint: Dp

    data object Small : WindowSize {
        override val breakpoint = 0.dp
    }

    data object Medium : WindowSize {
        override val breakpoint = 600.dp
    }

    data object Large : WindowSize {
        override val breakpoint = 1200.dp
    }
}

@Composable expect fun calculateWindowSize(): WindowSize
