package interaction

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun PressableWithEmphasis(content: @Composable PressableWithEmphasisScope.() -> Unit) {
    PressableWithEmphasisScope(remember { MutableInteractionSource() }, 1f).content()
}
