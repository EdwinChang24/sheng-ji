package io.github.edwinchang24.shengjidisplay.previews

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.edwinchang24.shengjidisplay.components.PlayingCard
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.PlayingCard
import io.github.edwinchang24.shengjidisplay.model.Suit
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Preview
@Composable
private fun PlayingCardPreview() {
    ShengJiDisplayTheme(AppState.Prop(AppState()) {}) {
        Surface {
            PlayingCard(
                card = PlayingCard("A", Suit.SPADES),
                state = AppState.Prop(AppState()) {},
                textStyle = LocalTextStyle.current.copy(fontSize = 128.sp)
            )
        }
    }
}
