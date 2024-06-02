package io.github.edwinchang24.shengjidisplay.display

data class DisplayContentPair(val topContent: DisplayContent, val bottomContent: DisplayContent)

infix fun DisplayContent.and(bottomContent: DisplayContent) =
    DisplayContentPair(this, bottomContent)
