package model

import arrow.optics.optics
import kotlinx.serialization.Serializable

@Serializable
@optics
data class Call(val card: PlayingCard, val number: Int, val found: Int) {
    companion object
}
