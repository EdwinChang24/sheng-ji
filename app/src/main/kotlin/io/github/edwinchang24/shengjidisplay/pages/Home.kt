package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.BuildConfig
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.components.PlayingCard
import io.github.edwinchang24.shengjidisplay.destinations.DisplayPageDestination
import io.github.edwinchang24.shengjidisplay.destinations.EditCallDialogDestination
import io.github.edwinchang24.shengjidisplay.destinations.EditTrumpDialogDestination
import io.github.edwinchang24.shengjidisplay.destinations.SettingsPageDestination
import io.github.edwinchang24.shengjidisplay.util.formatCallNumber

@OptIn(ExperimentalMaterial3Api::class)
@Destination(style = HomePageTransitions::class)
@MainNavGraph(start = true)
@Composable
fun HomePage(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.app_name)) }, actions = {
            IconButton(onClick = { navigator.navigate(SettingsPageDestination) }) {
                Icon(painterResource(R.drawable.ic_display_settings), null)
            }
        })
    }) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Trump card", style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            if (state.trump != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigator.navigate(EditTrumpDialogDestination) }
                        .padding(horizontal = 24.dp, vertical = 16.dp)) {
                    PlayingCard(state.trump!!, textStyle = LocalTextStyle.current.copy(fontSize = 32.sp))
                    IconButton(onClick = { viewModel.state.value = state.copy(trump = null) }) {
                        Icon(painterResource(R.drawable.ic_close), null)
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigator.navigate(EditTrumpDialogDestination) }
                        .padding(horizontal = 24.dp)) {
                    Text("No trump card selected")
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { navigator.navigate(EditTrumpDialogDestination) }) {
                        Icon(painterResource(R.drawable.ic_add), null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add")
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text("Calls", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                if (state.calls.isNotEmpty()) {
                    OutlinedButton(onClick = { viewModel.state.value = state.copy(calls = emptyList()) }) {
                        Icon(painterResource(R.drawable.ic_clear_all), null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear all")
                    }
                }
            }
            if (state.calls.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(state.calls) { index, call ->
                        OutlinedCard(onClick = { navigator.navigate(EditCallDialogDestination(index)) }) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)
                            ) {
                                PlayingCard(call.card, textStyle = LocalTextStyle.current.copy(fontSize = 32.sp))
                                Text(formatCallNumber(call.number))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.small)
                                        .clickable {
                                            viewModel.state.value = state.copy(calls = state.calls
                                                .toMutableList()
                                                .apply { this[index] = this[index].copy(found = !call.found) })
                                        }
                                        .padding(horizontal = 8.dp)) {
                                    Text("Found?")
                                    Checkbox(checked = call.found, onCheckedChange = {
                                        viewModel.state.value = state.copy(calls = state.calls.toMutableList()
                                            .apply { this[index] = this[index].copy(found = it) })
                                    })
                                }
                                IconButton(onClick = {
                                    viewModel.state.value =
                                        state.copy(calls = state.calls.toMutableList().apply { removeAt(index) })
                                }) {
                                    Icon(painterResource(R.drawable.ic_close), null)
                                }
                            }
                        }
                    }
                    item {
                        OutlinedButton(onClick = { navigator.navigate(EditCallDialogDestination(state.calls.size)) }) {
                            Icon(painterResource(R.drawable.ic_add), null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add call")
                        }
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigator.navigate(EditCallDialogDestination(0)) }
                        .padding(horizontal = 24.dp)) {
                    Text("No calls added")
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { navigator.navigate(EditCallDialogDestination(0)) }) {
                        Icon(painterResource(R.drawable.ic_add), null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add")
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navigator.navigate(DisplayPageDestination) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
            ) {
                Icon(painterResource(R.drawable.ic_smart_display), null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start display")
            }
            Text(
                "Version ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

object HomePageTransitions : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() = fadeIn()
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() = fadeOut()
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition() = fadeIn()
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() = fadeOut()
}
