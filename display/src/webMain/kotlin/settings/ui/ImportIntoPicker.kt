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
import resources.Res
import resources.ic_done
import settings.ImportInto
import util.iconRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsImportIntoPicker(
    importInto: ImportInto,
    setImportInto: (ImportInto) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text("Import quick transfers into", maxLines = 2, overflow = TextOverflow.Ellipsis)
        var expanded by rememberSaveable { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
        ) {
            OutlinedTextField(
                value = importInto.readableName,
                onValueChange = {},
                readOnly = true,
                leadingIcon = { Icon(iconRes(importInto.icon), null) },
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
                for (selection in listOf(ImportInto.Here, ImportInto.Android, ImportInto.Ask)) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                selection.readableName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = { Icon(iconRes(selection.icon), null) },
                        trailingIcon = {
                            if (selection == importInto) Icon(iconRes(Res.drawable.ic_done), null)
                        },
                        onClick = {
                            setImportInto(selection)
                            expanded = false
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}
