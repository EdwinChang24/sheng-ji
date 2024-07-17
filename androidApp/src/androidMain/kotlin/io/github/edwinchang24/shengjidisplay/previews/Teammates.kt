package io.github.edwinchang24.shengjidisplay.previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.edwinchang24.shengjidisplay.components.Teammates
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme
import kotlin.math.PI

@Preview
@Composable
private fun TeammatesPreview() {
    ShengJiDisplayTheme(AppState.Prop(AppState()) {}) {
        Surface {
            Teammates(
                editing = false,
                savedTeammatesRad =
                    listOf(
                            0f,
                            1f,
                            (PI / 2).toFloat(),
                            PI.toFloat(),
                            3f * (PI / 2).toFloat(),
                            4f,
                            5f
                        )
                        .mapIndexed { index, fl -> "$index" to fl }
                        .toMap(),
                setSavedTeammatesRad = {},
                onDone = {}
            )
        }
    }
}
