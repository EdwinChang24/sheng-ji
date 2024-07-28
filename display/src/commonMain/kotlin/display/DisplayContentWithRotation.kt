package display

data class DisplayContentWithRotation(
    val displayContentPair: DisplayContentPair,
    val rotation: ContentRotation
)

infix fun DisplayContentPair.with(rotation: ContentRotation) =
    DisplayContentWithRotation(this, rotation)
