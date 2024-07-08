package io.github.edwinchang24.shengjidisplay.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun iconRes(res: DrawableResource) =
    rememberVectorPainter(
        vectorResource(res).takeUnless { it.name == "emptyImageVector" }
            ?: ImageVector.Builder(
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 1f,
                    viewportHeight = 1f
                )
                .build()
    )

@Composable
fun suitIconRes(res: DrawableResource) =
    rememberVectorPainter(
        vectorResource(res).takeUnless { it.name == "emptyImageVector" }
            ?: ImageVector.Builder(
                    defaultWidth = 64.dp,
                    defaultHeight = 64.dp,
                    viewportWidth = 1f,
                    viewportHeight = 1f
                )
                .build()
    )
