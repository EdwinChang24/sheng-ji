package io.github.edwinchang24.shengjidisplay.components

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
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis

@Composable
fun ButtonWithEmphasis(
    text: String,
    icon: Painter? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation()
) {
    PressableWithEmphasis {
        Button(
            onClick = onClick,
            enabled = enabled,
            elevation = elevation,
            interactionSource = interactionSource,
            modifier = modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.pressEmphasis()
            ) {
                icon?.let { Icon(it, null) }
                Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors()
) {
    PressableWithEmphasis {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
            modifier = modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.pressEmphasis()
            ) {
                icon?.let { Icon(it, null) }
                Text(text, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun IconButtonWithEmphasis(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    PressableWithEmphasis {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = modifier.pointerHoverIcon(PointerIcon.Hand)
        ) {
            Box(modifier = Modifier.pressEmphasis()) { content() }
        }
    }
}
