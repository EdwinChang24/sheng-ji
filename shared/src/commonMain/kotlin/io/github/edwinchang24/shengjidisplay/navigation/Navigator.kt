package io.github.edwinchang24.shengjidisplay.navigation

interface Navigator {
    fun navigate(screen: Screen)

    fun toggleSettings()

    fun navigate(dialog: Dialog)

    fun closeDialog()
}
