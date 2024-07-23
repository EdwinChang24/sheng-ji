package display

sealed interface ContentRotation {
    data object Center : ContentRotation

    data object TopTowardsRight : ContentRotation

    data object BottomTowardsRight : ContentRotation
}
