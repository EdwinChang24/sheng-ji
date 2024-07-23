package settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import util.ExpandHeights
import util.ExpandWidthsScope
import util.WeightRow

@Composable
fun ExpandWidthsScope.SettingsBooleanPicker(
    value: Boolean,
    setValue: (Boolean) -> Unit,
    label: @Composable () -> Unit
) {
    ExpandHeights {
        WeightRow(
            modifier =
                Modifier.expandWidth()
                    .clickable { setValue(!value) }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.expandHeight()) {
                label()
            }
            Spacer(modifier = Modifier.weight())
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.expandHeight()) {
                Checkbox(checked = value, onCheckedChange = { setValue(it) })
            }
        }
    }
}
