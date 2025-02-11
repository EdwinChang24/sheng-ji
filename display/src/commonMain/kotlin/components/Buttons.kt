package components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import interaction.PressableWithEmphasis
import util.DefaultTransition

@Composable
fun ButtonWithEmphasis(
    text: String,
    icon: Painter? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
) {
    PressableWithEmphasis {
        Button(
            onClick = onClick,
            enabled = enabled,
            elevation = elevation,
            interactionSource = interactionSource,
            modifier =
                modifier.then(
                    if (enabled) Modifier.pointerHoverIcon(PointerIcon.Hand) else Modifier
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.pressEmphasis(),
            ) {
                AnimatedContent(
                    targetState = icon,
                    transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
                ) { iconState ->
                    iconState?.let { Icon(it, null) }
                }
                AnimatedContent(
                    targetState = text,
                    transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
                ) { textState ->
                    Text(textState, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun OutlinedButtonWithEmphasis(
    text: String,
    icon: Painter? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
) {
    PressableWithEmphasis {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
            modifier =
                modifier.then(
                    if (enabled) Modifier.pointerHoverIcon(PointerIcon.Hand) else Modifier
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.pressEmphasis(),
            ) {
                AnimatedContent(
                    targetState = icon,
                    transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
                ) { iconState ->
                    iconState?.let { Icon(it, null) }
                }
                AnimatedContent(
                    targetState = text,
                    transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
                ) { textState ->
                    Text(textState, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun IconButtonWithEmphasis(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    PressableWithEmphasis {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            modifier =
                modifier.then(
                    if (enabled) Modifier.pointerHoverIcon(PointerIcon.Hand) else Modifier
                ),
        ) {
            Box(modifier = Modifier.pressEmphasis()) { content() }
        }
    }
}
