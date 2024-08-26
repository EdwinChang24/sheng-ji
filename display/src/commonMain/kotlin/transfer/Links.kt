package transfer

import kotlin.math.PI
import kotlin.math.roundToInt
import model.Call
import model.PlayingCard
import model.Suit
import util.allRanks

const val BaseUrl = "https://shengji-dev.edwinchang.dev/display"
const val BaseUrlWww = "https://www.shengji-dev.edwinchang.dev/display"
const val AndroidLinksUrl = "https://l.shengji-dev.edwinchang.dev/"

fun TransferredData.toUrl(): String {
    val possibleTrumpsEncoded =
        allRanks
            .joinToString(separator = "") { if (it in (possibleTrumps ?: emptySet())) "1" else "0" }
            .toInt(2)
            .takeIf { it > 0 } ?: ""
    val trumpEncoded = trump?.encoded() ?: ""
    val callsEncoded =
        calls?.joinToString(separator = "") { "${it.card.encoded()}${it.found}-${it.number}" } ?: ""
    val teammatesEncoded =
        teammates?.joinToString(separator = "") {
            ((it + PI) * 9999 / (PI * 2)).roundToInt().coerceIn(0..9999).toString().padStart(4, '0')
        } ?: ""
    return "$BaseUrl?d=$possibleTrumpsEncoded.$trumpEncoded.$callsEncoded.$teammatesEncoded"
}

fun PlayingCard.encoded() =
    (when (suit) {
            Suit.CLUBS -> 'A'
            Suit.DIAMONDS -> 'N'
            Suit.HEARTS -> 'a'
            Suit.SPADES -> 'n'
        }.code + allRanks.indexOf(rank))
        .toChar()

fun decodePlayingCard(encoded: Char) =
    when (encoded) {
        in 'A'..'M' -> PlayingCard(allRanks[encoded - 'A'], Suit.CLUBS)
        in 'N'..'Z' -> PlayingCard(allRanks[encoded - 'N'], Suit.DIAMONDS)
        in 'a'..'m' -> PlayingCard(allRanks[encoded - 'a'], Suit.HEARTS)
        in 'n'..'z' -> PlayingCard(allRanks[encoded - 'n'], Suit.SPADES)
        else -> null
    }

fun dataParamFromUrl(url: String) =
    (if (url.startsWith(BaseUrl)) url.removePrefix(BaseUrl)
        else if (url.startsWith(BaseUrlWww)) url.removePrefix(BaseUrlWww) else null)
        ?.removePrefix("/")
        ?.split('?', '&')
        ?.firstOrNull { it.startsWith("d=") }
        ?.removePrefix("d=")

fun decodeParam(dataParam: String): TransferredData {
    val raw = dataParam.split('.')
    val possibleTrumps =
        raw.getOrNull(0)
            ?.toIntOrNull()
            ?.toString(2)
            ?.padStart(allRanks.size, '0')
            ?.takeLast(allRanks.size)
            ?.mapIndexedNotNull { index, c -> if (c == '1') allRanks[index] else null }
            ?.takeIf { it.isNotEmpty() }
            ?.toSet()
    val trump = raw.getOrNull(1)?.firstOrNull()?.let { decodePlayingCard(it) }
    val calls =
        raw.getOrNull(2)?.let { str ->
            val cards = str.map { decodePlayingCard(it) }.filterNotNull()
            val numbers =
                str.split(*(('A'..'Z') + ('a'..'z')).toCharArray())
                    .filter { it.isNotEmpty() }
                    .map { it.split('-') }
            cards
                .mapIndexed { index, card ->
                    numbers.getOrNull(index)?.getOrNull(1)?.toIntOrNull()?.coerceAtLeast(1)?.let {
                        number ->
                        numbers
                            .getOrNull(index)
                            ?.getOrNull(0)
                            ?.toIntOrNull()
                            ?.coerceIn(1..number)
                            ?.let { found -> Call(card = card, number = number, found = found) }
                    }
                }
                .filterNotNull()
                .takeIf { it.isNotEmpty() }
        }
    val teammates =
        raw.getOrNull(3)
            ?.chunked(4)
            ?.mapNotNull { it.toDoubleOrNull()?.div(9999)?.times(PI * 2)?.minus(PI)?.toFloat() }
            ?.takeIf { it.isNotEmpty() }
    return TransferredData(possibleTrumps, trump, calls, teammates)
}
