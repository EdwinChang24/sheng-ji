package io.github.edwinchang24.shengjidisplay.display

import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.VerticalOrder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DisplayScheme {
    fun getPossibleContentPairs(state: AppState): List<DisplayContentPair>

    @SerialName("main")
    @Serializable
    data object Main : DisplayScheme {
        override fun getPossibleContentPairs(state: AppState): List<DisplayContentPair> {
            val showCalls =
                !(state.settings.autoHideCalls && state.calls.all { it.found == it.number })
            return if (showCalls) {
                when (state.settings.verticalOrder) {
                    VerticalOrder.Auto ->
                        listOf(
                            DisplayContent.Trump and DisplayContent.Calls,
                            DisplayContent.Calls and DisplayContent.Trump
                        )
                    VerticalOrder.TrumpOnTop ->
                        listOf(DisplayContent.Trump and DisplayContent.Calls)
                    VerticalOrder.CallsOnTop ->
                        listOf(DisplayContent.Calls and DisplayContent.Trump)
                }
            } else {
                listOf(DisplayContent.Trump and DisplayContent.Trump)
            }
        }
    }

    @SerialName("possibleTrumps")
    @Serializable
    data object PossibleTrumps : DisplayScheme {
        override fun getPossibleContentPairs(state: AppState) =
            listOf(DisplayContent.PossibleTrumps and DisplayContent.PossibleTrumps)
    }
}
