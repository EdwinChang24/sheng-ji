package teammates

import androidx.compose.runtime.Composable

@Composable expect fun TeammatesBackHandler(enabled: Boolean = true, onBack: () -> Unit)
