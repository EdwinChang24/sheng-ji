package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Composable
fun NumberPicker(value: Int, setValue: (Int) -> Unit, modifier: Modifier = Modifier, range: IntRange = 1..10) {
    LaunchedEffect(value) { if (value !in range) setValue(value.coerceIn(range)) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        OutlinedIconButton(onClick = { if (value - 1 in range) setValue(value - 1) }, enabled = value - 1 in range) {
            Icon(painterResource(R.drawable.ic_remove), null)
        }
        Text(value.toString())
        OutlinedIconButton(onClick = { if (value + 1 in range) setValue(value + 1) }, enabled = value + 1 in range) {
            Icon(painterResource(R.drawable.ic_add), null)
        }
    }
}

@Preview
@Composable
private fun NumberPickerPreview() {
    ShengJiDisplayTheme {
        Surface {
            var num by rememberSaveable { mutableIntStateOf(1) }
            NumberPicker(num, { num = it })
        }
    }
}
