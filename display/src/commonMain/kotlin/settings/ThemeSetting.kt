package settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import resources.Res
import resources.ic_dark_mode
import resources.ic_light_mode
import resources.ic_palette

@Serializable
sealed interface ThemeSetting {
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
    data object System : ThemeSetting {
        override val name = "system"
        override val readableName = "Follow system"
        override val icon = Res.drawable.ic_palette
    }

    @Serializable
    @SerialName("dark")
    data object Dark : ThemeSetting {
        override val name = "dark"
        override val readableName = "Dark"
        override val icon = Res.drawable.ic_dark_mode
    }

    @Serializable
    @SerialName("light")
    data object Light : ThemeSetting {
        override val name = "light"
        override val readableName = "Light"
        override val icon = Res.drawable.ic_light_mode
    }
}
