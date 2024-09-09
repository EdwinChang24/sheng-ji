package settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import resources.Res
import resources.ic_done
import resources.ic_timer
import util.iconRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAutoSwitchSecondsPicker(
    autoSwitchSeconds: Int,
    setAutoSwitchSeconds: (Int) -> Unit,
    enabled: Boolean,
) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
    ) {
        Text(
            "Auto switch interval",
            color = LocalContentColor.current.copy(alpha = if (enabled) 1f else 0.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        var expanded by rememberSaveable { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true),
        ) {
            OutlinedTextField(
                value = autoSwitchIntervals[autoSwitchSeconds] ?: "$autoSwitchSeconds seconds",
                onValueChange = {},
                enabled = enabled,
                readOnly = true,
                leadingIcon = { Icon(iconRes(Res.drawable.ic_timer), null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
            ) {
                autoSwitchIntervals.forEach { (seconds, name) ->
                    DropdownMenuItem(
                        text = { Text(name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        trailingIcon = {
                            if (seconds == autoSwitchSeconds)
                                Icon(iconRes(Res.drawable.ic_done), null)
                        },
                        onClick = {
                            setAutoSwitchSeconds(seconds)
                            expanded = false
                            focusManager.clearFocus()
                        },
                    )
                }
            }
        }
    }
}

private val autoSwitchIntervals =
    mapOf(
        3 to "3 seconds",
        5 to "5 seconds",
        10 to "10 seconds",
        20 to "20 seconds",
        60 to "1 minute",
        300 to "5 minutes",
        1_577_847_600 to "50 years",
    )
