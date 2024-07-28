package settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import util.ExpandWidthsScope
import util.WeightRow

@Composable
fun ExpandWidthsScope.SettingsBooleanPicker(
    value: Boolean,
    setValue: (Boolean) -> Unit,
    label: @Composable () -> Unit
) {
    WeightRow(
        modifier =
            Modifier.expandWidth()
                .clickable { setValue(!value) }
                .pointerHoverIcon(PointerIcon.Hand)
                .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        label()
        Spacer(modifier = Modifier.weight())
        Checkbox(checked = value, onCheckedChange = { setValue(it) })
    }
}
