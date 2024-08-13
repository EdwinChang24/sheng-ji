package transfer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.ButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import model.AppState
import model.platformSettings
import navigation.Dialog
import navigation.Navigator
import resources.Res
import resources.ic_close
import resources.ic_code
import settings.ImportInto
import settings.importInto
import settings.web
import util.ExpandWidths
import util.WindowWidth
import util.iconRes

@Composable
fun DisambigDialog(
    url: String,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier
) {
    ExpandWidths(modifier = Modifier.widthIn(0.dp, WindowWidth.Medium.breakpoint)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            Text(
                "Quick transfer",
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                """You clicked on a link to import data, but it looks like you're on Android. Would you like to import into the web app or the Android app?""",
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.expandWidth()
            ) {
                var saveChoice by rememberSaveable { mutableStateOf(false) }
                ButtonWithEmphasis(
                    text = "Import here",
                    icon = iconRes(Res.drawable.ic_code),
                    onClick = {
                        removeUrlParamWeb()
                        if (saveChoice) {
                            state { AppState.platformSettings.web.importInto set ImportInto.Here }
                        }
                        navigator.navigate(Dialog.QuickTransfer(url = url))
                    }
                )
                ButtonWithEmphasis(
                    text = "Import into Android app",
                    icon = iconRes(Res.drawable.ic_code),
                    onClick = {
                        if (saveChoice) {
                            state { AppState.platformSettings.web.importInto set ImportInto.Here }
                        }
                        openUrlWeb("$AndroidLinksUrl${dataParamFromUrl(url)}")
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier.clip(MaterialTheme.shapes.medium)
                            .clickable { saveChoice = !saveChoice }
                            .pointerHoverIcon(PointerIcon.Hand)
                            .padding(horizontal = 8.dp)
                ) {
                    Checkbox(checked = saveChoice, onCheckedChange = { saveChoice = it })
                    Text(
                        "Save my choice",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                OutlinedButtonWithEmphasis(
                    text = "Don't import",
                    icon = iconRes(Res.drawable.ic_close),
                    onClick = {
                        removeUrlParamWeb()
                        navigator.closeDialog()
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

expect fun openUrlWeb(url: String)

expect fun removeUrlParamWeb()
