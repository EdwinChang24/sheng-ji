package io.github.edwinchang24.shengjidisplay.model

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

    @Serializable @SerialName("web") data object Web : PlatformSettings

    companion object
}

expect val defaultPlatformSettings: PlatformSettings
