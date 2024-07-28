package settings

import display.ContentRotation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ContentRotationSetting {
    val possibleRotations: List<ContentRotation>

    @Serializable
    @SerialName("auto")
    data object Auto : ContentRotationSetting {
        override val possibleRotations =
            listOf(
                ContentRotation.TopTowardsRight,
                ContentRotation.Center,
                ContentRotation.BottomTowardsRight
            )
    }

    @Serializable
    @SerialName("center")
    data object Center : ContentRotationSetting {
        override val possibleRotations = listOf(ContentRotation.Center)
    }

    @Serializable
    @SerialName("topTowardsRight")
    data object TopTowardsRight : ContentRotationSetting {
        override val possibleRotations = listOf(ContentRotation.TopTowardsRight)
    }

    @Serializable
    @SerialName("bottomTowardsRight")
    data object BottomTowardsRight : ContentRotationSetting {
        override val possibleRotations = listOf(ContentRotation.BottomTowardsRight)
    }
}
