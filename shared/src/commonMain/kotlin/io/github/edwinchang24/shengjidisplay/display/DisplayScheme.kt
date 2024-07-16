package io.github.edwinchang24.shengjidisplay.display

import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.MainDisplayOrder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DisplayScheme {
    val showTeammates: Boolean

    fun getPossibleContentPairs(state: AppState): List<DisplayContentPair>

    @SerialName("main")
    @Serializable
    data object Main : DisplayScheme {
        override val showTeammates = true

        override fun getPossibleContentPairs(state: AppState): List<DisplayContentPair> {
            val showCalls =
                !(state.settings.mainDisplay.autoHideCalls &&
                    state.calls.all { it.found == it.number })
            return if (showCalls) {
                when (state.settings.mainDisplay.displayOrder) {
                    MainDisplayOrder.Auto ->
                        listOf(
                            DisplayContent.Trump and DisplayContent.Calls,
                            DisplayContent.Calls and DisplayContent.Trump
                        )
                    MainDisplayOrder.TrumpOnTop ->
                        listOf(DisplayContent.Trump and DisplayContent.Calls)
                    MainDisplayOrder.CallsOnTop ->
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
        override val showTeammates = false

        override fun getPossibleContentPairs(state: AppState) =
            listOf(DisplayContent.PossibleTrumps and DisplayContent.PossibleTrumps)
    }
}
