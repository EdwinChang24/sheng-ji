package dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import appVersion
import components.AppName
import components.ButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.AppState
import navigation.Navigator
import org.jetbrains.compose.resources.painterResource
import resources.Res
import resources.hearts
import resources.ic_code
import resources.ic_code_blocks
import resources.ic_done
import resources.ic_license
import resources.ic_public
import resources.logo_edwin_chang_dark
import resources.logo_edwin_chang_light
import resources.logo_sheng_ji_dark
import resources.logo_sheng_ji_display
import resources.logo_sheng_ji_light
import util.ExpandWidths
import util.PlatformName
import util.iconRes
import util.suitIconRes

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutDialog(navigator: Navigator, state: AppState.Prop, modifier: Modifier = Modifier) {
    ExpandWidths {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier =
                modifier.verticalScroll(rememberScrollState()).widthIn(0.dp, 480.dp).padding(24.dp),
        ) {
            Text(
                "About",
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Image(
                painter = painterResource(Res.drawable.logo_sheng_ji_display),
                null,
                modifier = Modifier.size(128.dp).align(Alignment.CenterHorizontally),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.expandWidth().padding(horizontal = 16.dp),
            ) {
                ProvideTextStyle(MaterialTheme.typography.titleLarge) { AppName() }
                Text(
                    "Version $appVersion for $PlatformName",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            val uriHandler = LocalUriHandler.current
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier.align(Alignment.CenterHorizontally)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { uriHandler.openUri("https://edwinchang.dev") }
                        .pointerHoverIcon(PointerIcon.Hand)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
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
                            ),
                    )
                    Text(
                        buildAnnotatedString {
                            append(" by ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) { append("Edwin") }
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 2,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 16.dp),
            ) {
                OutlinedButtonWithEmphasis(
                    text = "Website",
                    icon = iconRes(Res.drawable.ic_public),
                    onClick = { uriHandler.openUri("https://shengji.edwinchang.dev") },
                )
                OutlinedButtonWithEmphasis(
                    text = "Source code",
                    icon = iconRes(Res.drawable.ic_code),
                    onClick = { uriHandler.openUri("https://github.com/EdwinChang24/sheng-ji") },
                )
                OutlinedButtonWithEmphasis(
                    text = "Dependencies",
                    icon = painterResource(Res.drawable.ic_code_blocks),
                    onClick = {
                        uriHandler.openUri(
                            "https://github.com/EdwinChang24/sheng-ji/blob/-/display/NOTICE"
                        )
                    },
                )
                OutlinedButtonWithEmphasis(
                    text = "License",
                    icon = iconRes(Res.drawable.ic_license),
                    onClick = {
                        uriHandler.openUri(
                            "https://github.com/EdwinChang24/sheng-ji/blob/-/LICENSE"
                        )
                    },
                )
            }
            val credits1 =
                "Special thanks to Wen, Daniel, Jayleen, Alice, Lorenz, Kai, Hannah, Eli, " +
                    "Bianca, Brendan, Seb, Nathan, Anirvan, Kevin, Ronin, May, Steven, Kina, " +
                    "Sophia, Jerry, Wendy, and Ellie for giving me the best Saturday nights of " +
                    "my life."
            val credits2 =
                "Super special thanks to Cynthia and her family for teaching us the game and " +
                    "hosting us nocturnal party animals."
            val credits3 = "And to Bunny for being unconditionally a goofball."
            Text(credits1, textAlign = TextAlign.Center, modifier = Modifier.expandWidth())
            Text(credits2, textAlign = TextAlign.Center, modifier = Modifier.expandWidth())
            Text(credits3, textAlign = TextAlign.Center, modifier = Modifier.expandWidth())
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.expandWidth(),
            ) {
                Image(
                    painter =
                        painterResource(
                            if (state().settings.general.theme.computesToDark())
                                Res.drawable.logo_sheng_ji_dark
                            else Res.drawable.logo_sheng_ji_light
                        ),
                    null,
                    modifier =
                        Modifier.clip(MaterialTheme.shapes.medium)
                            .clickable { uriHandler.openUri("https://shengji.edwinchang.dev") }
                            .pointerHoverIcon(PointerIcon.Hand)
                            .padding(16.dp)
                            .size(64.dp),
                )
                Image(
                    painter =
                        painterResource(
                            if (state().settings.general.theme.computesToDark())
                                Res.drawable.logo_edwin_chang_dark
                            else Res.drawable.logo_edwin_chang_light
                        ),
                    null,
                    modifier =
                        Modifier.clip(MaterialTheme.shapes.medium)
                            .clickable { uriHandler.openUri("https://edwinchang.dev") }
                            .pointerHoverIcon(PointerIcon.Hand)
                            .padding(16.dp)
                            .size(64.dp),
                )
            }
            val year = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
            Text(
                "© $year Edwin Chang",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier =
                    Modifier.align(Alignment.CenterHorizontally)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { uriHandler.openUri("https://edwinchang.dev") }
                        .pointerHoverIcon(PointerIcon.Hand)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            ButtonWithEmphasis(
                text = "Done",
                icon = iconRes(Res.drawable.ic_done),
                onClick = { navigator.closeDialog() },
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}
