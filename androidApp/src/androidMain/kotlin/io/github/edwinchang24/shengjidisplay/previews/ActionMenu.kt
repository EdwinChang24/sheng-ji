package io.github.edwinchang24.shengjidisplay.previews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.edwinchang24.shengjidisplay.components.ActionMenu
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme

@Preview
@Composable
private fun ActionMenuPreview() {
    ShengJiDisplayTheme(AppState.Prop(AppState()) {}) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ActionMenu(
                onAction = {},
                canPause = true,
                pause = false,
                showTeammates = true,
                editingTeammates = false,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
