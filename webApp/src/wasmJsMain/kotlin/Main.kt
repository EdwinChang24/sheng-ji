import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.edwinchang24.shengjidisplay.components.CallsDisplay
import io.github.edwinchang24.shengjidisplay.model.Call
import io.github.edwinchang24.shengjidisplay.model.PlayingCard
import io.github.edwinchang24.shengjidisplay.model.Suit
import io.github.edwinchang24.shengjidisplay.theme.ShengJiDisplayTheme
import org.jetbrains.compose.resources.configureWebResources

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    configureWebResources { resourcePathMapping { path -> "./$path" } }
    CanvasBasedWindow(canvasElementId = "app") {
        ShengJiDisplayTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.padding(16.dp)) {
                    CallsDisplay(
                        listOf(
                            Call(PlayingCard("A", Suit.SPADES), 1, 0),
                            Call(PlayingCard("A", Suit.HEARTS), 1, 0)
                        ),
                        setFound = { _, _ -> }
                    )
                }
            }
        }
    }
}
