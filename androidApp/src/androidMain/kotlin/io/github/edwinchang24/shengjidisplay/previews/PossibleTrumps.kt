package io.github.edwinchang24.shengjidisplay.previews

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.components.PossibleTrumps
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Preview
@Composable
private fun PossibleTrumpsPreview(modifier: Modifier = Modifier) {
    ShengJiDisplayTheme {
        Surface(modifier = Modifier.size(500.dp)) { PossibleTrumps(setOf("5", "10", "K")) }
    }
}
