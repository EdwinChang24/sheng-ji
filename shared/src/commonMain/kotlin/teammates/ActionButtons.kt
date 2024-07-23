package teammates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.ButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import resources.Res
import resources.ic_clear_all
import resources.ic_done
import resources.ic_undo
import util.iconRes

@Composable
fun TeammatesActionButtons(
    hasRecentlyCleared: Boolean,
    canClear: Boolean,
    onPressClear: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        OutlinedButtonWithEmphasis(
            text = if (!hasRecentlyCleared) "Clear" else "Undo",
            icon =
                iconRes(
                    if (!hasRecentlyCleared) Res.drawable.ic_clear_all else Res.drawable.ic_undo
                ),
            onClick = onPressClear,
            enabled = canClear || hasRecentlyCleared,
            colors =
                ButtonDefaults.outlinedButtonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.surface)
        )
        Spacer(modifier = Modifier.height(mainButtonRadiusDp * 2 + 16.dp))
        ButtonWithEmphasis(
            text = "Done",
            icon = iconRes(Res.drawable.ic_done),
            onClick = onDone,
            elevation = ButtonDefaults.buttonElevation(6.dp, 6.dp, 6.dp, 8.dp, 6.dp)
        )
    }
}
