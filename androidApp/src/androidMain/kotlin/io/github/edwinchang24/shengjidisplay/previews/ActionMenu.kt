package io.github.edwinchang24.shengjidisplay.previews

import actionmenu.ActionMenu
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import model.AppState
import theme.ShengJiDisplayTheme

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
