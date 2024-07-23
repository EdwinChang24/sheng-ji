package home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.ButtonWithEmphasis
import display.DisplayScheme
import navigation.Navigator
import navigation.Screen
import resources.Res
import resources.ic_smart_display
import util.iconRes

@Composable
fun StartButtons(navigator: Navigator, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        ButtonWithEmphasis(
            text = "Start possible trumps display",
            icon = iconRes(Res.drawable.ic_smart_display),
            onClick = { navigator.navigate(Screen.Display(scheme = DisplayScheme.PossibleTrumps)) },
            elevation = ButtonDefaults.buttonElevation(6.dp, 6.dp, 6.dp, 8.dp, 6.dp)
        )
        ButtonWithEmphasis(
            text = "Start main display",
            icon = iconRes(Res.drawable.ic_smart_display),
            onClick = { navigator.navigate(Screen.Display(scheme = DisplayScheme.Main)) },
            elevation = ButtonDefaults.buttonElevation(6.dp, 6.dp, 6.dp, 8.dp, 6.dp)
        )
    }
}
