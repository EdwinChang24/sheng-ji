package io.github.edwinchang24.shengjidisplay.navigation

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed interface Dialog {
    val title: String

    @Serializable
    @SerialName("editCall")
    data class EditCall(val index: Int) : Dialog {
        override val title = "Edit call"
    }

    @Serializable
    @SerialName("editPossibleTrumps")
    data object EditPossibleTrumps : Dialog {
        override val title = "Edit possible trumps"
    }

    @Serializable
    @SerialName("editTrump")
    data object EditTrump : Dialog {
        override val title = "Edit trump card"
    }

    companion object {
        val Saver =
            Saver<Dialog?, String>(
                save = { Json.encodeToString(it) },
                restore = { Json.decodeFromString(it) }
            )
    }
}
