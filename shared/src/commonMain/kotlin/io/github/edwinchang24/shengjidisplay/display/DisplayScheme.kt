package io.github.edwinchang24.shengjidisplay.display

import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.VerticalOrder

interface DisplayScheme {
    fun getPossibleContentPairs(state: AppState): List<DisplayContentPair>

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
}
