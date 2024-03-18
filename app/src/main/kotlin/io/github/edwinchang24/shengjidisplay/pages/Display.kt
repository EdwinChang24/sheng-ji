package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import io.github.edwinchang24.shengjidisplay.MainNavGraph

@Destination
@MainNavGraph
@Composable
fun DisplayPage() {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("TODO")
        }
    }
}
