package navigation

import androidx.compose.runtime.saveable.Saver
import display.DisplayScheme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed interface Screen {
    @Serializable @SerialName("home") data object Home : Screen

    @Serializable
    @SerialName("display")
    data class Display(val scheme: DisplayScheme, val editTeammates: Boolean = false) : Screen

    companion object {
        val Saver =
            Saver<Screen, String>(
                save = { Json.encodeToString(it) },
                restore = { Json.decodeFromString(it) }
            )
    }
}
