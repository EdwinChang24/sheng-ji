package io.github.edwinchang24.shengjidisplay.previews

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import io.github.edwinchang24.shengjidisplay.components.SuitPicker
import io.github.edwinchang24.shengjidisplay.model.Suit
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Preview
@Composable
private fun SuitPickerPreview() {
    ShengJiDisplayTheme {
        Surface {
            var suit: Suit? by rememberSaveable { mutableStateOf(null) }
            SuitPicker(suit = suit, { suit = it })
        }
    }
}
