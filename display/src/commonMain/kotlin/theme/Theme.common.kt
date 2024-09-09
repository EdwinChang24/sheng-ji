package theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import model.AppState

@Composable expect fun ShengJiDisplayTheme(state: AppState.Prop, content: @Composable () -> Unit)

val defaultLightTheme =
    lightColorScheme(
        primary = Color(0xFF4B5C92),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFDBE1FF),
        onPrimaryContainer = Color(0xFF00174B),
        secondary = Color(0xFF2C638B),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFCCE5FF),
        onSecondaryContainer = Color(0xFF001D31),
        tertiary = Color(0xFF07677F),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFB7EAFF),
        onTertiaryContainer = Color(0xFF001F28),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFAF8FF),
        onBackground = Color(0xFF1A1B21),
        surface = Color(0xFFFAF8FF),
        onSurface = Color(0xFF1A1B21),
        surfaceVariant = Color(0xFFE2E2EC),
        onSurfaceVariant = Color(0xFF45464F),
        outline = Color(0xFF757680),
        outlineVariant = Color(0xFFC5C6D0),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFF2F3036),
        inverseOnSurface = Color(0xFFF1F0F7),
        inversePrimary = Color(0xFFB4C5FF),
        surfaceDim = Color(0xFFDAD9E0),
        surfaceBright = Color(0xFFFAF8FF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF4F3FA),
        surfaceContainer = Color(0xFFEEEDF4),
        surfaceContainerHigh = Color(0xFFE8E7EF),
        surfaceContainerHighest = Color(0xFFE3E2E9),
    )

val defaultDarkTheme =
    darkColorScheme(
        primary = Color(0xFFB4C5FF),
        onPrimary = Color(0xFF1A2D60),
        primaryContainer = Color(0xFF324478),
        onPrimaryContainer = Color(0xFFDBE1FF),
        secondary = Color(0xFF99CCFA),
        onSecondary = Color(0xFF003351),
        secondaryContainer = Color(0xFF074B72),
        onSecondaryContainer = Color(0xFFCCE5FF),
        tertiary = Color(0xFF88D1EC),
        onTertiary = Color(0xFF003543),
        tertiaryContainer = Color(0xFF004E61),
        onTertiaryContainer = Color(0xFFB7EAFF),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF121318),
        onBackground = Color(0xFFE3E2E9),
        surface = Color(0xFF121318),
        onSurface = Color(0xFFE3E2E9),
        surfaceVariant = Color(0xFF45464F),
        onSurfaceVariant = Color(0xFFC5C6D0),
        outline = Color(0xFF8F909A),
        outlineVariant = Color(0xFF45464F),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFFE3E2E9),
        inverseOnSurface = Color(0xFF2F3036),
        inversePrimary = Color(0xFF4B5C92),
        surfaceDim = Color(0xFF121318),
        surfaceBright = Color(0xFF38393F),
        surfaceContainerLowest = Color(0xFF0D0E13),
        surfaceContainerLow = Color(0xFF1A1B21),
        surfaceContainer = Color(0xFF1E1F25),
        surfaceContainerHigh = Color(0xFF292A2F),
        surfaceContainerHighest = Color(0xFF34343A),
    )
