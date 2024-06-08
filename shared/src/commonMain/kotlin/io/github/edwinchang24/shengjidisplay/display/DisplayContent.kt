package io.github.edwinchang24.shengjidisplay.display

sealed interface DisplayContent {
    val name: String

    data object None : DisplayContent {
        override val name: String = ""
    }

    data object Trump : DisplayContent {
        override val name = "Trump card"
    }

    data object Calls : DisplayContent {
        override val name = "Calls"
    }

    data object PossibleTrumps : DisplayContent {
        override val name = "Possible trumps"
    }
}
