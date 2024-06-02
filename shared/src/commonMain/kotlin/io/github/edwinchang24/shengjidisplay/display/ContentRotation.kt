package io.github.edwinchang24.shengjidisplay.display

sealed interface ContentRotation {
    data object Center : ContentRotation

    data object TopTowardsRight : ContentRotation

    data object BottomTowardsRight : ContentRotation
}
