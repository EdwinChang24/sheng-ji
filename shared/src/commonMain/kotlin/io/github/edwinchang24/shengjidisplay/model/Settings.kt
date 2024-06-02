package io.github.edwinchang24.shengjidisplay.model

import io.github.edwinchang24.shengjidisplay.display.ContentRotation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val keepScreenOn: Boolean = true,
    val lockScreenOrientation: Boolean = true,
    val fullScreen: Boolean = true,
    val autoHideCalls: Boolean = true,
    val verticalOrder: VerticalOrder = VerticalOrder.Auto,
    val contentRotation: ContentRotationSetting = ContentRotationSetting.Center,
    val autoSwitchSeconds: Int = 5,
    val showClock: Boolean = true,
    val clockOrientation: Boolean = true
)

@Serializable
sealed interface VerticalOrder {
    @Serializable @SerialName("auto") data object Auto : VerticalOrder

    @Serializable @SerialName("trumpOnTop") data object TrumpOnTop : VerticalOrder

    @Serializable @SerialName("callsOnTop") data object CallsOnTop : VerticalOrder
}

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
