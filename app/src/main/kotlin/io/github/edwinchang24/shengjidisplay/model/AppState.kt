package io.github.edwinchang24.shengjidisplay.model

import kotlinx.serialization.Serializable

@Serializable
data class AppState(
    val trump: PlayingCard? = null,
    val calls: List<Call> = emptyList(),
    val settings: Settings = Settings()
)
