package io.github.edwinchang24.shengjidisplay.model

import kotlinx.serialization.Serializable

@Serializable
data class AppState(
    val trump: PlayingCard? = null,
    val calls: List<Call> = emptyList(),
    val teammates: Map<String, Float> = emptyMap(),
    val possibleTrumps: Set<String> = emptySet(),
    val settings: Settings = Settings()
)
