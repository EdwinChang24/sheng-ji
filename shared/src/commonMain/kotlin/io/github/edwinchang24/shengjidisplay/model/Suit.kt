package io.github.edwinchang24.shengjidisplay.model

import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.clubs
import io.github.edwinchang24.shengjidisplay.resources.diamonds
import io.github.edwinchang24.shengjidisplay.resources.hearts
import io.github.edwinchang24.shengjidisplay.resources.spades
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.compose.resources.DrawableResource

@Serializable(SuitSerializer::class)
enum class Suit(val asString: String, val icon: DrawableResource) {
    CLUBS("clubs", Res.drawable.clubs),
    DIAMONDS("diamonds", Res.drawable.diamonds),
    HEARTS("hearts", Res.drawable.hearts),
    SPADES("spades", Res.drawable.spades)
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
