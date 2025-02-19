package previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import components.RankPicker
import model.AppState
import theme.ShengJiDisplayTheme

@Preview
@Composable
private fun RankPickerPreview() {
    ShengJiDisplayTheme(AppState.Prop(AppState()) {}) {
        Surface {
            var rank: String? by rememberSaveable { mutableStateOf(null) }
            RankPicker(rank, { rank = it })
        }
    }
}
