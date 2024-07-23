package settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainDisplayOrder {
    @Serializable @SerialName("auto") data object Auto : MainDisplayOrder

    @Serializable @SerialName("trumpOnTop") data object TrumpOnTop : MainDisplayOrder

    @Serializable @SerialName("callsOnTop") data object CallsOnTop : MainDisplayOrder
}
