package transfer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import model.AppState
import org.jetbrains.compose.resources.decodeToImageBitmap
import qrcode.QRCode
import qrcode.color.Colors

@Composable
actual fun QrImage(data: String, state: AppState.Prop, modifier: Modifier) {
    var image: ImageBitmap? by remember { mutableStateOf(null) }
    val useDarkTheme = state().settings.general.theme.computesToDark()
    LaunchedEffect(data) {
        image =
            QRCode.ofSquares()
                .withRadius(0)
                .withInnerSpacing(0)
                .withBackgroundColor(Colors.TRANSPARENT)
                .withColor(if (useDarkTheme) Colors.WHITE else Colors.BLACK)
                .build(data)
                .renderToBytes()
                .decodeToImageBitmap()
    }
    image?.let {
        Image(
            bitmap = it,
            null,
            modifier =
                modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(if (useDarkTheme) Color.Black else Color.White)
                    .padding(8.dp)
        )
    }
}
