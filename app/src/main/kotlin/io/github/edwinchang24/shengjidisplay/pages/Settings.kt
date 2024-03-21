package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import io.github.edwinchang24.shengjidisplay.BuildConfig
import io.github.edwinchang24.shengjidisplay.MainActivityViewModel
import io.github.edwinchang24.shengjidisplay.MainNavGraph
import io.github.edwinchang24.shengjidisplay.R
import io.github.edwinchang24.shengjidisplay.model.HorizontalOrientation
import io.github.edwinchang24.shengjidisplay.model.VerticalOrder

@OptIn(ExperimentalMaterial3Api::class)
@Destination(style = SettingsPageTransitions::class)
@MainNavGraph
@Composable
fun SettingsPage(navigator: DestinationsNavigator, viewModel: MainActivityViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Display settings") }, navigationIcon = {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(painterResource(R.drawable.ic_arrow_back), null)
            }
        })
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            BooleanPicker(value = state.settings.keepScreenOn, setValue = {
                viewModel.state.value = state.copy(settings = state.settings.copy(keepScreenOn = it))
            }) { Text("Keep screen on") }
            BooleanPicker(value = state.settings.lockScreenOrientation, setValue = {
                viewModel.state.value = state.copy(settings = state.settings.copy(lockScreenOrientation = it))
            }) { Text("Lock screen orientation to portrait") }
            BooleanPicker(value = state.settings.autoHideCalls, setValue = {
                viewModel.state.value = state.copy(settings = state.settings.copy(autoHideCalls = it))
            }) { Text("Hide calls when all are found") }
            VerticalOrderPicker(verticalOrder = state.settings.verticalOrder, setVerticalOrder = {
                viewModel.state.value = state.copy(settings = state.settings.copy(verticalOrder = it))
            })
            BooleanPicker(value = state.settings.perpendicularMode, setValue = {
                viewModel.state.value = state.copy(settings = state.settings.copy(perpendicularMode = it))
            }) { Text("Perpendicular mode") }
            AnimatedVisibility(
                visible = state.settings.perpendicularMode,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top, clip = false),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top, clip = false)
            ) {
                // @formatter:off
                HorizontalOrientationPicker(
                    horizontalOrientation = state.settings.horizontalOrientation,
                    setHorizontalOrientation = {
                        viewModel.state.value = state.copy(settings = state.settings.copy(horizontalOrientation = it))
                    }
                )
                // @formatter:on
            }
            AnimatedVisibility(
                visible = state.settings.verticalOrder == VerticalOrder.Auto || (state.settings.perpendicularMode && state.settings.horizontalOrientation == HorizontalOrientation.Auto),
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top, clip = false),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top, clip = false)
            ) {
                AutoSwitchSecondsPicker(autoSwitchSeconds = state.settings.autoSwitchSeconds, setAutoSwitchSeconds = {
                    viewModel.state.value = state.copy(settings = state.settings.copy(autoSwitchSeconds = it))
                })
            }
            BooleanPicker(value = state.settings.showClock, setValue = {
                viewModel.state.value = state.copy(settings = state.settings.copy(showClock = it))
            }) { Text("Show clock") }
            Text(
                "${stringResource(R.string.app_name)} ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun BooleanPicker(value: Boolean, setValue: (Boolean) -> Unit, label: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { setValue(!value) }
            .padding(horizontal = 24.dp, vertical = 8.dp)) {
        label()
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(checked = value, onCheckedChange = { setValue(it) })
    }
}

@Composable
private fun RowScope.PickerCard(onClick: () -> Unit, selected: Boolean, content: @Composable ColumnScope.() -> Unit) {
    OutlinedCard(
        // @formatter:off
        onClick = onClick,
        border = BorderStroke(
            width = animateDpAsState(if (selected) 4.dp else CardDefaults.outlinedCardBorder().width, label = "").value,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        content = content
        // @formatter:on
    )
}

@Composable
private fun VerticalOrderPicker(verticalOrder: VerticalOrder, setVerticalOrder: (VerticalOrder) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text("Vertical ordering")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            PickerCard(
                onClick = { setVerticalOrder(VerticalOrder.Auto) }, selected = verticalOrder == VerticalOrder.Auto
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Auto switch", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                }
            }
            PickerCard(
                onClick = { setVerticalOrder(VerticalOrder.TrumpOnTop) },
                selected = verticalOrder == VerticalOrder.TrumpOnTop
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    Text("Trump", textAlign = TextAlign.Center, modifier = Modifier.rotate(180f))
                    HorizontalDivider()
                    Text("Calls", textAlign = TextAlign.Center)
                }
            }
            PickerCard(
                onClick = { setVerticalOrder(VerticalOrder.CallsOnTop) },
                selected = verticalOrder == VerticalOrder.CallsOnTop
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    Text("Calls", textAlign = TextAlign.Center, modifier = Modifier.rotate(180f))
                    HorizontalDivider()
                    Text("Trump", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun HorizontalOrientationPicker(
    horizontalOrientation: HorizontalOrientation, setHorizontalOrientation: (HorizontalOrientation) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text("Horizontal orientation")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            PickerCard(
                onClick = { setHorizontalOrientation(HorizontalOrientation.Auto) },
                selected = horizontalOrientation == HorizontalOrientation.Auto
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Auto switch", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                }
            }
            PickerCard(
                onClick = { setHorizontalOrientation(HorizontalOrientation.TopTowardsRight) },
                selected = horizontalOrientation == HorizontalOrientation.TopTowardsRight
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    Text("Aa", textAlign = TextAlign.Center, modifier = Modifier.rotate(-90f))
                    HorizontalDivider()
                    Text("Aa", textAlign = TextAlign.Center, modifier = Modifier.rotate(90f))
                }
            }
            PickerCard(
                onClick = { setHorizontalOrientation(HorizontalOrientation.BottomTowardsRight) },
                selected = horizontalOrientation == HorizontalOrientation.BottomTowardsRight
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    Text("Aa", textAlign = TextAlign.Center, modifier = Modifier.rotate(90f))
                    HorizontalDivider()
                    Text("Aa", textAlign = TextAlign.Center, modifier = Modifier.rotate(-90f))
                }
            }
        }
    }
}

private val autoSwitchIntervals = mapOf(
    3 to "3 seconds", 5 to "5 seconds", 10 to "10 seconds", 20 to "20 seconds", 60 to "1 minute", 300 to "5 minutes"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AutoSwitchSecondsPicker(autoSwitchSeconds: Int, setAutoSwitchSeconds: (Int) -> Unit) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text("Auto switch interval")
        var expanded by rememberSaveable { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            // @formatter:off
            OutlinedTextField(
                value = autoSwitchIntervals[autoSwitchSeconds] ?: "$autoSwitchSeconds seconds",
                onValueChange = {},
                readOnly = true,
                leadingIcon = { Icon(painterResource(R.drawable.ic_timer), null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            // @formatter:on
            DropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            }) {
                autoSwitchIntervals.forEach { (seconds, name) ->
                    DropdownMenuItem(text = { Text(name) }, onClick = {
                        setAutoSwitchSeconds(seconds)
                        expanded = false
                        focusManager.clearFocus()
                    })
                }
            }
        }
    }
}

object SettingsPageTransitions : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() =
        slideInHorizontally { fullWidth -> fullWidth }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() =
        slideOutHorizontally { fullWidth -> fullWidth }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition() =
        slideInHorizontally { fullWidth -> fullWidth }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() =
        slideOutHorizontally { fullWidth -> fullWidth }
}
