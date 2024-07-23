package settings

import arrow.optics.optics
import kotlinx.serialization.Serializable

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
            val theme: ThemeSetting = ThemeSetting.System,
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
