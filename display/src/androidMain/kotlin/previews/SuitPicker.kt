package previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import components.SuitPicker
import model.AppState
import model.Suit
import theme.ShengJiDisplayTheme

@Preview
@Composable
private fun SuitPickerPreview() {
    ShengJiDisplayTheme(AppState.Prop(AppState()) {}) {
        Surface {
            var suit: Suit? by rememberSaveable { mutableStateOf(null) }
            SuitPicker(suit = suit, { suit = it }, AppState.Prop(AppState()) {})
        }
    }
}
