package io.github.edwinchang24.shengjidisplay.navigation

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed interface Dialog {

    @Serializable @SerialName("editCall") data class EditCall(val index: Int) : Dialog

    @Serializable @SerialName("editPossibleTrumps") data object EditPossibleTrumps : Dialog

    @Serializable @SerialName("editTrump") data object EditTrump : Dialog

    companion object {
        val Saver =
            Saver<Dialog?, String>(
                save = { Json.encodeToString(it) },
                restore = { Json.decodeFromString(it) }
            )
    }
}
