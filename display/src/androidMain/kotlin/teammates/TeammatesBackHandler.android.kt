package teammates

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun TeammatesBackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled, onBack)
