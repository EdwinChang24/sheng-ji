package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.sheng_ji
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AppName(modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        val density = LocalDensity.current
        var height by rememberSaveable { mutableStateOf(0f) }
        val painter =
            rememberVectorPainter(
                vectorResource(Res.drawable.sheng_ji).takeUnless { it.name == "emptyImageVector" }
                    ?: ImageVector.Builder(
                            defaultWidth = 155.36.dp,
                            defaultHeight = 75.36.dp,
                            viewportWidth = 1f,
                            viewportHeight = 1f
                        )
                        .build()
            )
        Image(
            painter,
            null,
            colorFilter = ColorFilter.tint(LocalContentColor.current),
            modifier = Modifier.height(with(density) { height.toDp() })
        )
        Text(
            " Display",
            minLines = 1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                height = it.layoutInput.style.fontSize.let { with(density) { it.toPx() } }
            }
        )
    }
}
