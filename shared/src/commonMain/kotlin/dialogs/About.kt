package dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import appVersion
import components.AppName
import components.ButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import navigation.Navigator
import resources.Res
import resources.hearts
import resources.ic_code
import resources.ic_done
import resources.ic_license
import util.ExpandWidths
import util.PlatformName
import util.WindowWidth
import util.calculateWindowWidth
import util.iconRes
import util.suitIconRes

@Composable
fun AboutDialog(navigator: Navigator, modifier: Modifier = Modifier) {
    ExpandWidths {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = modifier.verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            Text(
                "About",
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.expandWidth().padding(horizontal = 16.dp)
            ) {
                ProvideTextStyle(MaterialTheme.typography.titleLarge) { AppName() }
                Text(
                    "Version $appVersion for $PlatformName",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            val links =
                @Composable {
                    val uriHandler = LocalUriHandler.current
                    OutlinedButtonWithEmphasis(
                        text = "Source code",
                        icon = iconRes(Res.drawable.ic_code),
                        onClick = {
                            uriHandler.openUri("https://github.com/EdwinChang24/sheng-ji-display")
                        }
                    )
                    OutlinedButtonWithEmphasis(
                        text = "License",
                        icon = iconRes(Res.drawable.ic_license),
                        onClick = {
                            uriHandler.openUri(
                                "https://github.com/EdwinChang24/sheng-ji-display/blob/-/LICENSE"
                            )
                        }
                    )
                }
            val windowWidth = calculateWindowWidth()
            if (windowWidth <= WindowWidth.Small) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.expandWidth()
                ) {
                    links()
                }
            } else {
                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.expandWidth().padding(horizontal = 24.dp)
                ) {
                    links()
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.expandWidth()
            ) {
                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                    Text("Made with ", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Image(
                        suitIconRes(Res.drawable.hearts),
                        null,
                        colorFilter = ColorFilter.tint(Color.Red),
                        modifier =
                            Modifier.height(
                                with(LocalDensity.current) {
                                    LocalTextStyle.current.fontSize.toDp()
                                }
                            )
                    )
                    Text(
                        buildAnnotatedString {
                            append(" by ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) { append("Edwin") }
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            ButtonWithEmphasis(
                text = "Done",
                icon = iconRes(Res.drawable.ic_done),
                onClick = { navigator.closeDialog() },
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
