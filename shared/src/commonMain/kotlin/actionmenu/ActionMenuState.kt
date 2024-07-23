package actionmenu

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.unit.IntOffset

sealed interface ActionMenuState {
    data class Dragging(val offset: IntOffset) : ActionMenuState

    data object Opened : ActionMenuState

    data class Closed(val action: Action? = null) : ActionMenuState

    object StateSaver : Saver<ActionMenuState, Boolean> {
        override fun SaverScope.save(value: ActionMenuState) = value == Opened

        override fun restore(value: Boolean) = if (value) Opened else Closed()
    }
}
