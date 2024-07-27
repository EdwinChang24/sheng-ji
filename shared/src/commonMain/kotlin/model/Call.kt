package model

import arrow.optics.optics
import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable

@Serializable
@optics
data class Call(
    val id: String = uuid4().toString(),
    val card: PlayingCard,
    val number: Int,
    val found: Int
) {
    companion object
}
