package io.github.edwinchang24.shengjidisplay.model

import androidx.annotation.DrawableRes
import io.github.edwinchang24.shengjidisplay.R
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(SuitSerializer::class)
enum class Suit(val asString: String, @DrawableRes val icon: Int) {
    CLUBS("clubs", R.drawable.clubs),
    DIAMONDS("diamonds", R.drawable.diamonds),
    HEARTS("hearts", R.drawable.hearts),
    SPADES("spades", R.drawable.spades)
}

class SuitSerializer : KSerializer<Suit> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Suit", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Suit) = encoder.encodeString(value.asString)

    override fun deserialize(decoder: Decoder) =
        decoder.decodeString().let { str ->
            Suit.entries.firstOrNull { it.asString == str } ?: Suit.CLUBS
        }
}
