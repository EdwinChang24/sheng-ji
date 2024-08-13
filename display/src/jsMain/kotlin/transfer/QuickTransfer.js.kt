package transfer

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import model.AppState

@Composable
actual fun QrImage(data: String, state: AppState.Prop, modifier: Modifier) {
    var qrResult: QrCodeGenerateResult? by remember { mutableStateOf(null) }
    LaunchedEffect(data) { qrResult = encode(data, null) }
    qrResult?.let { result ->
        val useDarkTheme = state().settings.general.theme.computesToDark()
        Canvas(
            modifier =
                modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(if (useDarkTheme) Color.Black else Color.White)
                    .padding(8.dp)
        ) {
            val points =
                result.data.flatMapIndexed { rowIndex, row ->
                    row.mapIndexedNotNull { colIndex: Int, cell: Boolean ->
                        if (cell)
                            Offset(
                                (colIndex.toFloat() + 0.5f) * size.width / result.size,
                                (rowIndex.toFloat() + 0.5f) * size.height / result.size
                            )
                        else null
                    }
                }
            drawPoints(
                points = points,
                pointMode = PointMode.Points,
                color = if (useDarkTheme) Color.White else Color.Black,
                strokeWidth = ceil(size.maxDimension / result.size) + 1f,
                cap = StrokeCap.Square
            )
        }
    }
}
