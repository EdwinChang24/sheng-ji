package util

import androidx.compose.runtime.Composable
import display.LocalWindowState

@Composable actual fun calculateWindowWidth() = WindowWidth.from(LocalWindowState.current.size)

@Composable actual fun calculateWindowHeight() = WindowHeight.from(LocalWindowState.current.size)
