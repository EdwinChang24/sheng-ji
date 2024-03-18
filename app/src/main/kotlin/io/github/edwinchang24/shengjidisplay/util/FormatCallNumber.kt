package io.github.edwinchang24.shengjidisplay.util

fun formatCallNumber(number: Int) = number.toString().let {
    when (it.last().digitToInt()) {
        1 -> it + "st"
        2 -> it + "nd"
        3 -> it + "rd"
        else -> it + "th"
    }
}
