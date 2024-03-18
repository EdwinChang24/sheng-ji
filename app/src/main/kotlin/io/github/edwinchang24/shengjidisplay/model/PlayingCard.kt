package io.github.edwinchang24.shengjidisplay.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayingCard(val rank: String, val suit: Suit)
