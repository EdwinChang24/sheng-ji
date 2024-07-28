package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

sealed interface WindowWidth {
    companion object {
        fun from(size: DpSize) =
            listOf(Large, Medium, Small, ExtraSmall).firstOrNull { size.width >= it.breakpoint }
                ?: ExtraSmall
    }

    val breakpoint: Dp

    operator fun compareTo(width: WindowWidth) = breakpoint.compareTo(width.breakpoint)

    data object ExtraSmall : WindowWidth {
        override val breakpoint = 0.dp
    }

    data object Small : WindowWidth {
        override val breakpoint = 400.dp
    }

    data object Medium : WindowWidth {
        override val breakpoint = 600.dp
    }

    data object Large : WindowWidth {
        override val breakpoint = 1200.dp
    }
}

@Composable expect fun calculateWindowWidth(): WindowWidth

sealed interface WindowHeight {

    companion object {
        fun from(size: DpSize) =
            listOf(Large, Medium, Small).firstOrNull { size.height >= it.breakpoint } ?: Small
    }

    val breakpoint: Dp

    operator fun compareTo(height: WindowHeight) = breakpoint.compareTo(height.breakpoint)

    data object Small : WindowHeight {
        override val breakpoint = 0.dp
    }

    data object Medium : WindowHeight {
        override val breakpoint = 400.dp
    }

    data object Large : WindowHeight {
        override val breakpoint = 600.dp
    }
}

@Composable expect fun calculateWindowHeight(): WindowHeight
