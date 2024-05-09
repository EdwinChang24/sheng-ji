package io.github.edwinchang24.shengjidisplay.model

import kotlinx.serialization.Serializable

@Serializable data class Call(val card: PlayingCard, val number: Int, val found: Int)
