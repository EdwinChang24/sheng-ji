package io.github.edwinchang24.shengjidisplay.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.Suit
import org.jetbrains.compose.resources.painterResource

@Composable
fun SuitPicker(suit: Suit?, setSuit: (Suit) -> Unit, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
        Suit.entries.forEach {
            PressableWithEmphasis {
                OutlinedCard(
                    onClick = { setSuit(it) },
                    border =
                        BorderStroke(
                            width =
                                animateDpAsState(
                                        targetValue =
                                            if (suit == it) 4.dp
                                            else CardDefaults.outlinedCardBorder().width,
                                        label = "",
                                    )
                                    .value,
                            color =
                                if (suit == it) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant,
                        ),
                    interactionSource = interactionSource,
                    modifier = Modifier.weight(1f),
                ) {
                    Image(
                        painterResource(it.icon),
                        null,
                        modifier =
                            Modifier.pressEmphasis()
                                .padding(16.dp)
                                .size(32.dp)
                                .align(Alignment.CenterHorizontally),
                    )
                }
            }
        }
    }
}
