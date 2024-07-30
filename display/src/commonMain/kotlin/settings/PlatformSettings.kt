package settings

import arrow.optics.optics
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@optics
sealed interface PlatformSettings {
    @Serializable
    @SerialName("android")
    @optics
    data class Android(
        val fullScreen: Boolean = true,
        val lockOrientation: Boolean = true,
        val keepScreenOn: Boolean = true
    ) : PlatformSettings {
        companion object
    }

    @Serializable
    @SerialName("web")
    @optics
    data class Web(val keepScreenOn: Boolean = true) : PlatformSettings {
        companion object
    }

    @Serializable
    @SerialName("desktop")
    @optics
    data class Desktop(val fullScreen: Boolean = true) : PlatformSettings {
        companion object
    }

    companion object
}

expect val defaultPlatformSettings: PlatformSettings
