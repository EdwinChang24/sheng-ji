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
import settings.ThemeSetting
import util.iconRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemePicker(theme: ThemeSetting, setTheme: (ThemeSetting) -> Unit) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text("Theme", maxLines = 1, overflow = TextOverflow.Ellipsis)
        var expanded by rememberSaveable { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
        ) {
            OutlinedTextField(
                value = theme.readableName,
                onValueChange = {},
                readOnly = true,
                leadingIcon = { Icon(iconRes(theme.icon), null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                for (themeSelection in
                    listOf(ThemeSetting.System, ThemeSetting.Dark, ThemeSetting.Light)) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                themeSelection.readableName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = { Icon(iconRes(themeSelection.icon), null) },
                        onClick = {
                            setTheme(themeSelection)
                            expanded = false
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}
