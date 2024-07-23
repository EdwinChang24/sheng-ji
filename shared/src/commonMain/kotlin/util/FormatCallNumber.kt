package util

fun formatCallNumber(number: Int) =
    number.toString().let {
        if (it.getOrNull(it.length - 2)?.digitToInt() == 1) it + "th"
        else {
            when (it.last().digitToInt()) {
                1 -> it + "st"
                2 -> it + "nd"
                3 -> it + "rd"
                else -> it + "th"
            }
        }
    }
