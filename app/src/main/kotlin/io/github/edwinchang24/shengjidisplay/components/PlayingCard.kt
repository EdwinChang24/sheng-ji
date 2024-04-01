package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.edwinchang24.shengjidisplay.model.PlayingCard
import io.github.edwinchang24.shengjidisplay.model.Suit
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Composable
fun PlayingCard(
    card: PlayingCard,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(
            card.rank,
            style = textStyle,
            fontWeight = FontWeight.Bold,
            color =
                if (card.suit in setOf(Suit.HEARTS, Suit.DIAMONDS)) Color.Red
                else if (isSystemInDarkTheme()) Color.White else Color.Black
        )
        Image(
            painterResource(card.suit.icon),
            null,
            modifier = Modifier.size(with(LocalDensity.current) { textStyle.fontSize.toDp() })
        )
    }
}

@Preview
@Composable
private fun PlayingCardPreview() {
    ShengJiDisplayTheme {
        Surface {
            PlayingCard(
                card = PlayingCard("A", Suit.SPADES),
                textStyle = LocalTextStyle.current.copy(fontSize = 128.sp)
            )
        }
    }
}
