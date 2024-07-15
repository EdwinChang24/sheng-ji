package io.github.edwinchang24.shengjidisplay.model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
@optics
data class PlayingCard(val rank: String, val suit: Suit) {
    companion object
}
