package io.github.edwinchang24.shengjidisplay.previews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.edwinchang24.shengjidisplay.components.CallsDisplay
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.model.PlayingCard
import io.github.edwinchang24.shengjidisplay.model.Suit
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Preview
@Composable
private fun CallsDisplayPreview() {
    ShengJiDisplayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            var calls by remember {
                mutableStateOf(
                    listOf(
                        Call(PlayingCard("A", Suit.SPADES), number = 1, found = 0),
                        Call(PlayingCard("K", Suit.HEARTS), number = 2, found = 2)
                    )
                )
            }
            CallsDisplay(
                calls,
                { index, found ->
                    calls = calls.toMutableList().also { it[index] = it[index].copy(found = found) }
                },
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}
