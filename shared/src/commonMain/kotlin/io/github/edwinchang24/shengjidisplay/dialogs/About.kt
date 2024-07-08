package io.github.edwinchang24.shengjidisplay.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.VersionConfig
import io.github.edwinchang24.shengjidisplay.components.AppName
import io.github.edwinchang24.shengjidisplay.components.ButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.components.OutlinedButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.hearts
import io.github.edwinchang24.shengjidisplay.resources.ic_code
import io.github.edwinchang24.shengjidisplay.resources.ic_done
import io.github.edwinchang24.shengjidisplay.resources.ic_license
import io.github.edwinchang24.shengjidisplay.util.PlatformName
import io.github.edwinchang24.shengjidisplay.util.WindowSize
import io.github.edwinchang24.shengjidisplay.util.calculateWindowSize
import io.github.edwinchang24.shengjidisplay.util.iconRes
import io.github.edwinchang24.shengjidisplay.util.suitIconRes

@Composable
fun AboutDialog(navigator: Navigator, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier =
            modifier.width(IntrinsicSize.Max).verticalScroll(rememberScrollState()).padding(24.dp)
    ) {
        Text("About", style = MaterialTheme.typography.headlineMedium)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.titleLarge) { AppName() }
            Text(
                "Version ${VersionConfig.version} for $PlatformName",
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
        val windowSize = calculateWindowSize()
        if (windowSize == WindowSize.Small) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                links()
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                links()
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                Text("Made with ", maxLines = 1, overflow = TextOverflow.Ellipsis)
                Image(
                    suitIconRes(Res.drawable.hearts),
                    null,
                    modifier =
                        Modifier.height(
                            with(LocalDensity.current) { LocalTextStyle.current.fontSize.toDp() },
                        ),
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
