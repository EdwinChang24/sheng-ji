package previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import model.AppState
import teammates.Teammates
import theme.ShengJiDisplayTheme

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
                            5f,
                        )
                        .mapIndexed { index, fl -> "$index" to fl }
                        .toMap(),
                setSavedTeammatesRad = {},
                onDone = {},
            )
        }
    }
}
