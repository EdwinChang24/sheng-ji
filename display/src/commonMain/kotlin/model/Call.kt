package model

import arrow.optics.optics
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
@optics
data class Call
@OptIn(ExperimentalUuidApi::class)
constructor(
    val id: String = Uuid.random().toString(),
    val card: PlayingCard,
    val number: Int,
    val found: Int,
) {
    companion object
}
