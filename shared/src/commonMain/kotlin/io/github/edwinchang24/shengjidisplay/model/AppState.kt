package io.github.edwinchang24.shengjidisplay.model

import arrow.optics.Copy
import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
@optics
data class AppState(
    val trump: PlayingCard? = null,
    val calls: List<Call> = emptyList(),
    val teammates: Map<String, Float> = emptyMap(),
    val possibleTrumps: Set<String> = emptySet(),
    val settings: Settings = Settings(),
    val platformSettings: PlatformSettings = defaultPlatformSettings
) {
    data class Prop(val state: AppState, val update: (Copy<AppState>.() -> Unit) -> Unit) {
        operator fun invoke() = state

        operator fun invoke(copy: Copy<AppState>.() -> Unit) {
            update(copy)
        }
    }

    companion object
}
