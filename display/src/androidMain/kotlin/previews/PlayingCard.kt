package previews

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import components.PlayingCard
import model.AppState
import model.PlayingCard
import model.Suit
import theme.ShengJiDisplayTheme

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
