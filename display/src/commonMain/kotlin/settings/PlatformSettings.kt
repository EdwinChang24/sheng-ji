package settings

import arrow.optics.optics
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import resources.Res
import resources.ic_code

@Serializable
@optics
sealed interface PlatformSettings {
    @Serializable
    @SerialName("android")
    @optics
    data class Android(
        val fullScreen: Boolean = true,
        val lockOrientation: Boolean = true,
        val keepScreenOn: Boolean = true,
    ) : PlatformSettings {
        companion object
    }

    @Serializable
    @SerialName("web")
    @optics
    data class Web(val keepScreenOn: Boolean = true, val importInto: ImportInto = ImportInto.Ask) :
        PlatformSettings {
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

@Serializable
sealed interface ImportInto {
    val readableName: String
    val icon: DrawableResource

    @Serializable
    @SerialName("here")
    data object Here : ImportInto {
        override val icon = Res.drawable.ic_code
        override val readableName = "Here"
    }

    @Serializable
    @SerialName("android")
    data object Android : ImportInto {
        override val icon = Res.drawable.ic_code
        override val readableName = "Android app"
    }

    @Serializable
    @SerialName("ask")
    data object Ask : ImportInto {
        override val icon = Res.drawable.ic_code
        override val readableName = "Ask first"
    }
}
