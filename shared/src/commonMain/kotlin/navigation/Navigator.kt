package navigation

interface Navigator {
    fun navigate(screen: Screen)

    fun toggleSettings()

    fun navigate(dialog: Dialog)

    fun closeDialog()
}
