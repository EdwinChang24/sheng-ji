package previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import components.NumberPicker
import model.AppState
import theme.ShengJiDisplayTheme

@Preview
@Composable
private fun NumberPickerPreview() {
    ShengJiDisplayTheme(AppState.Prop(AppState()) {}) {
        Surface {
            var num by rememberSaveable { mutableIntStateOf(1) }
            NumberPicker(num, { num = it })
        }
    }
}
