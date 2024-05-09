package io.github.edwinchang24.shengjidisplay.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val keepScreenOn: Boolean = true,
    val lockScreenOrientation: Boolean = true,
    val autoHideCalls: Boolean = true,
    val verticalOrder: VerticalOrder = VerticalOrder.Auto,
    val perpendicularMode: Boolean = false,
    val horizontalOrientation: HorizontalOrientation = HorizontalOrientation.Auto,
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
sealed interface HorizontalOrientation {
    @Serializable @SerialName("auto") data object Auto : HorizontalOrientation

    @Serializable
    @SerialName("topTowardsRight")
    data object TopTowardsRight : HorizontalOrientation

    @Serializable
    @SerialName("bottomTowardsRight")
    data object BottomTowardsRight : HorizontalOrientation
}
