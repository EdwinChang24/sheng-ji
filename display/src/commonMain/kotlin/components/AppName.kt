package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import resources.Res
import resources.sheng_ji

@Composable
fun AppName(modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        val painter =
            rememberVectorPainter(
                vectorResource(Res.drawable.sheng_ji).takeUnless { it.name == "emptyImageVector" }
                    ?: ImageVector.Builder(
                            defaultWidth = 155.36.dp,
                            defaultHeight = 75.36.dp,
                            viewportWidth = 1f,
                            viewportHeight = 1f,
                        )
                        .build()
            )
        Image(
            painter,
            null,
            colorFilter = ColorFilter.tint(LocalContentColor.current),
            modifier =
                Modifier.height(
                    with(LocalDensity.current) { LocalTextStyle.current.fontSize.toDp() }
                ),
        )
        Text(" Display", minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
