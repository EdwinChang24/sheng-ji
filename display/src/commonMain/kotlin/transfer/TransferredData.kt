package transfer

import model.Call
import model.PlayingCard

data class TransferredData(
    val possibleTrumps: Set<String>?,
    val trump: PlayingCard?,
    val calls: List<Call>?,
    val teammates: List<Float>?,
) {
    fun isEmpty() = possibleTrumps == null && trump == null && calls == null && teammates == null
}
