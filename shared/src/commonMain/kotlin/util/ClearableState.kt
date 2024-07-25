package util

data class ClearableState<T>(
    val value: T,
    val setValue: (T) -> Unit,
    val clearValue: () -> Unit,
    val canUndoClear: Boolean,
    val undoClearValue: () -> Unit
)
