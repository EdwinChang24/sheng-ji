package settings.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsPickerCard(
    onClick: () -> Unit,
    selected: Boolean,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    OutlinedCard(
        onClick = onClick,
        border =
            BorderStroke(
                width =
                    animateDpAsState(
                            if (selected) 4.dp else CardDefaults.outlinedCardBorder().width,
                            label = "",
                        )
                        .value,
                color =
                    if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant,
            ),
        interactionSource = interactionSource,
        modifier = modifier,
        content = content,
    )
}
