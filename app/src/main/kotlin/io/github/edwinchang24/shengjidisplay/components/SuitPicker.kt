package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.model.Suit
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Composable
fun SuitPicker(suit: Suit?, setSuit: (Suit) -> Unit, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
        Suit.entries.forEach {
            Card(
                onClick = { setSuit(it) },
                border = if (suit == it) BorderStroke(4.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(it.icon), null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


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
