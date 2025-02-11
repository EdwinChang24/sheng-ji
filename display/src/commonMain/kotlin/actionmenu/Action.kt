package actionmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DrawableResource
import resources.Res
import resources.ic_close
import resources.ic_fit_screen
import resources.ic_group
import resources.ic_pause
import resources.ic_play_arrow
import resources.ic_screen_rotation
import resources.ic_settings

@Serializable
sealed class Action {
    abstract val name: @Composable () -> String
    abstract val icon: @Composable () -> DrawableResource
    abstract val description: @Composable () -> String
    abstract val enabled: @Composable () -> Boolean

    @Serializable
    @SerialName("pauseResume")
    data class PauseResume(private val canPause: Boolean, private val pause: Boolean) : Action() {
        override val name = @Composable { if (pause) "Resume" else "Pause" }
        override val icon: @Composable () -> DrawableResource
            get() = { if (pause) Res.drawable.ic_play_arrow else Res.drawable.ic_pause }

        override val description =
            @Composable {
                """${if (pause) "Resume" else "Pause"} automatic switching of vertical order""" +
                    """ and/or content rotation."""
            }
        override val enabled = @Composable { canPause }

        override fun equals(other: Any?) = other is PauseResume

        override fun hashCode() = this::class.hashCode()
    }

    @Serializable
    @SerialName("teammates")
    data class Teammates(private val showTeammates: Boolean) : Action() {
        override val name = @Composable { "Teammates" }
        override val icon: @Composable () -> DrawableResource
            get() = { Res.drawable.ic_group }

        override val description = @Composable { """Edit the positions of the teammate arrows.""" }
        override val enabled = @Composable { showTeammates }
    }

    @Serializable
    @SerialName("settings")
    data object Settings : Action() {
        override val name = @Composable { "Settings" }
        override val icon = @Composable { Res.drawable.ic_settings }
        override val description = @Composable { """Open the settings menu.""" }
        override val enabled = @Composable { true }
    }

    @Serializable
    @SerialName("exit")
    data object Exit : Action() {
        override val name = @Composable { "Exit" }
        override val icon = @Composable { Res.drawable.ic_close }
        override val description =
            @Composable { """Exit the display and return to the home page.""" }
        override val enabled = @Composable { true }
    }

    @Serializable
    @SerialName("rotate")
    data object Rotate : Action() {
        override val name = @Composable { "Rotate" }
        override val icon = @Composable { Res.drawable.ic_screen_rotation }
        override val description =
            @Composable { """Rotate the display between horizontal and vertical.""" }
        override val enabled = @Composable { true }
    }

    @Serializable
    @SerialName("scale")
    data object Scale : Action() {
        override val name = @Composable { "Scale" }
        override val icon = @Composable { Res.drawable.ic_fit_screen }
        override val description = @Composable { """Change the display scale for this display.""" }
        override val enabled = @Composable { true }
    }

    object StateSaver : Saver<Action?, String> {
        override fun restore(value: String): Action? = Json.decodeFromString(value)

        override fun SaverScope.save(value: Action?) = Json.encodeToString(value)
    }
}
