package io.github.edwinchang24.shengjidisplay.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import arrow.optics.optics
import io.github.edwinchang24.shengjidisplay.display.ContentRotation
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_dark_mode
import io.github.edwinchang24.shengjidisplay.resources.ic_light_mode
import io.github.edwinchang24.shengjidisplay.resources.ic_palette
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
@optics
data class Settings(
    val general: General = General(),
    val mainDisplay: MainDisplay = MainDisplay(),
    val possibleTrumpsDisplay: PossibleTrumpsDisplay = PossibleTrumpsDisplay()
) {
    companion object {
        @Serializable
        @optics
        data class General(
            val theme: Theme = Theme.System,
            val displayRotationVertical: Boolean = true,
            val contentRotation: ContentRotationSetting = ContentRotationSetting.Center,
            val autoSwitchSeconds: Int = 5,
            val showClock: Boolean = true,
            val clockOrientation: Boolean = true,
            val underline6And9: Boolean = true
        ) {
            companion object
        }

        @Serializable
        @optics
        data class MainDisplay(
            val scale: Float = 1f,
            val autoHideCalls: Boolean = true,
            val displayOrder: MainDisplayOrder = MainDisplayOrder.Auto,
            val tapTrumpToEdit: Boolean = false
        ) {
            companion object
        }

        @Serializable
        @optics
        data class PossibleTrumpsDisplay(val scale: Float = 1f, val tapToEdit: Boolean = false) {
            companion object
        }
    }
}

@Serializable
sealed interface Theme {
    val name: String
    val readableName: String
    val icon: DrawableResource

    @Composable
    fun computesToDark() =
        when (this) {
            System -> isSystemInDarkTheme()
            Dark -> true
            Light -> false
        }

    @Serializable
    @SerialName("system")
    data object System : Theme {
        override val name = "system"
        override val readableName = "Follow system"
        override val icon = Res.drawable.ic_palette
    }

    @Serializable
    @SerialName("dark")
    data object Dark : Theme {
        override val name = "dark"
        override val readableName = "Dark"
        override val icon = Res.drawable.ic_dark_mode
    }

    @Serializable
    @SerialName("light")
    data object Light : Theme {
        override val name = "light"
        override val readableName = "Light"
        override val icon = Res.drawable.ic_light_mode
    }
}

@Serializable
sealed interface MainDisplayOrder {
    @Serializable @SerialName("auto") data object Auto : MainDisplayOrder

    @Serializable @SerialName("trumpOnTop") data object TrumpOnTop : MainDisplayOrder

    @Serializable @SerialName("callsOnTop") data object CallsOnTop : MainDisplayOrder
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
