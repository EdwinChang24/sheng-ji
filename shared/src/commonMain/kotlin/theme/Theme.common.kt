package theme

import androidx.compose.runtime.Composable
import model.AppState

@Composable expect fun ShengJiDisplayTheme(state: AppState.Prop, content: @Composable () -> Unit)
