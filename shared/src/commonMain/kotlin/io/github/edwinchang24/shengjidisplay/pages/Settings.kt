package io.github.edwinchang24.shengjidisplay.pages

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.edwinchang24.shengjidisplay.components.IconButtonWithEmphasis
import io.github.edwinchang24.shengjidisplay.interaction.PressableWithEmphasis
import io.github.edwinchang24.shengjidisplay.model.AppState
import io.github.edwinchang24.shengjidisplay.model.ContentRotationSetting
import io.github.edwinchang24.shengjidisplay.model.MainDisplayOrder
import io.github.edwinchang24.shengjidisplay.model.Theme
import io.github.edwinchang24.shengjidisplay.model.autoHideCalls
import io.github.edwinchang24.shengjidisplay.model.autoSwitchSeconds
import io.github.edwinchang24.shengjidisplay.model.contentRotation
import io.github.edwinchang24.shengjidisplay.model.displayOrder
import io.github.edwinchang24.shengjidisplay.model.general
import io.github.edwinchang24.shengjidisplay.model.mainDisplay
import io.github.edwinchang24.shengjidisplay.model.possibleTrumpsDisplay
import io.github.edwinchang24.shengjidisplay.model.settings
import io.github.edwinchang24.shengjidisplay.model.showClock
import io.github.edwinchang24.shengjidisplay.model.tapToEdit
import io.github.edwinchang24.shengjidisplay.model.tapTrumpToEdit
import io.github.edwinchang24.shengjidisplay.model.theme
import io.github.edwinchang24.shengjidisplay.model.underline6And9
import io.github.edwinchang24.shengjidisplay.navigation.Navigator
import io.github.edwinchang24.shengjidisplay.resources.Res
import io.github.edwinchang24.shengjidisplay.resources.ic_arrow_back
import io.github.edwinchang24.shengjidisplay.resources.ic_timer
import io.github.edwinchang24.shengjidisplay.util.iconRes
import io.github.edwinchang24.shengjidisplay.util.rotate90

@Composable
fun SettingsPage(navigator: Navigator, state: AppState.Prop, modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .width(IntrinsicSize.Max)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            IconButtonWithEmphasis(onClick = { navigator.toggleSettings() }) {
                Icon(iconRes(Res.drawable.ic_arrow_back), null)
            }
            Text("Display settings", style = MaterialTheme.typography.titleLarge)
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            SectionHeader("General")
            ThemePicker(
                theme = state().settings.general.theme,
                setTheme = { state { AppState.settings.general.theme set it } }
            )
            ContentRotationPicker(state)
            AutoSwitchSecondsPicker(
                autoSwitchSeconds = state().settings.general.autoSwitchSeconds,
                setAutoSwitchSeconds = {
                    state { AppState.settings.general.autoSwitchSeconds set it }
                },
                enabled =
                    state().settings.mainDisplay.displayOrder == MainDisplayOrder.Auto ||
                        state().settings.general.contentRotation == ContentRotationSetting.Auto
            )
            BooleanPicker(
                value = state().settings.general.showClock,
                setValue = { state { AppState.settings.general.showClock set it } }
            ) {
                Text("Show clock")
            }
            BooleanPicker(
                value = state().settings.general.underline6And9,
                setValue = { state { AppState.settings.general.underline6And9 set it } }
            ) {
                Text("Underline 6s and 9s")
            }
            SectionHeader("Main display")
            MainDisplayOrderPicker(state)
            BooleanPicker(
                value = state().settings.mainDisplay.autoHideCalls,
                setValue = { state { AppState.settings.mainDisplay.autoHideCalls set it } }
            ) {
                Text("Hide calls when all are found")
            }
            BooleanPicker(
                value = state().settings.mainDisplay.tapTrumpToEdit,
                setValue = { state { AppState.settings.mainDisplay.tapTrumpToEdit set it } }
            ) {
                Text("Tap trump card in display to edit")
            }
            SectionHeader("Possible trumps display")
            BooleanPicker(
                value = state().settings.possibleTrumpsDisplay.tapToEdit,
                setValue = { state { AppState.settings.possibleTrumpsDisplay.tapToEdit set it } }
            ) {
                Text("Tap display to edit")
            }
            PlatformSettings(
                state,
                { text, modifier1 -> SectionHeader(text, modifier1) },
                { value, setValue, label -> BooleanPicker(value, setValue, label) }
            )
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
expect fun PlatformSettings(
    state: AppState.Prop,
    sectionHeader: @Composable (text: String, modifier: Modifier) -> Unit,
    booleanPicker:
        @Composable
        (value: Boolean, setValue: (Boolean) -> Unit, label: @Composable () -> Unit) -> Unit
)

@Composable
private fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp)
    )
}

@Composable
private fun BooleanPicker(
    value: Boolean,
    setValue: (Boolean) -> Unit,
    label: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier.fillMaxWidth()
                .clickable { setValue(!value) }
                .pointerHoverIcon(PointerIcon.Hand)
                .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        label()
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(checked = value, onCheckedChange = { setValue(it) })
    }
}

@Composable
private fun RowScope.PickerCard(
    onClick: () -> Unit,
    selected: Boolean,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        border =
            BorderStroke(
                width =
                    animateDpAsState(
                            if (selected) 4.dp else CardDefaults.outlinedCardBorder().width,
                            label = ""
                        )
                        .value,
                color =
                    if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant
            ),
        interactionSource = interactionSource,
        modifier = Modifier.weight(1f).fillMaxHeight(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemePicker(theme: Theme, setTheme: (Theme) -> Unit) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text("Theme", maxLines = 1, overflow = TextOverflow.Ellipsis)
        var expanded by rememberSaveable { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
        ) {
            OutlinedTextField(
                value = theme.readableName,
                onValueChange = {},
                readOnly = true,
                leadingIcon = { Icon(iconRes(theme.icon), null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                for (themeSelection in listOf(Theme.System, Theme.Dark, Theme.Light)) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                themeSelection.readableName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = { Icon(iconRes(themeSelection.icon), null) },
                        onClick = {
                            setTheme(themeSelection)
                            expanded = false
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MainDisplayOrderPicker(state: AppState.Prop) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text("Display order")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
        ) {
            PressableWithEmphasis {
                PickerCard(
                    onClick = {
                        state {
                            AppState.settings.mainDisplay.displayOrder set MainDisplayOrder.Auto
                        }
                    },
                    selected = state().settings.mainDisplay.displayOrder == MainDisplayOrder.Auto,
                    interactionSource = interactionSource
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            "Auto switch",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp).pressEmphasis()
                        )
                    }
                }
            }
            PressableWithEmphasis {
                PickerCard(
                    onClick = {
                        state {
                            AppState.settings.mainDisplay.displayOrder set
                                MainDisplayOrder.TrumpOnTop
                        }
                    },
                    selected =
                        state().settings.mainDisplay.displayOrder == MainDisplayOrder.TrumpOnTop,
                    interactionSource = interactionSource
                ) {
                    if (state().settings.general.displayRotationVertical) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize().padding(16.dp).pressEmphasis()
                        ) {
                            Text(
                                "Trump",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate(180f)
                            )
                            HorizontalDivider()
                            Text(
                                "Calls",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize().padding(16.dp).pressEmphasis()
                        ) {
                            Text(
                                "Trump",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90()
                            )
                            VerticalDivider()
                            Text(
                                "Calls",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90(negative = true)
                            )
                        }
                    }
                }
            }
            PressableWithEmphasis {
                PickerCard(
                    onClick = {
                        state {
                            AppState.settings.mainDisplay.displayOrder set
                                MainDisplayOrder.CallsOnTop
                        }
                    },
                    selected =
                        state().settings.mainDisplay.displayOrder == MainDisplayOrder.CallsOnTop,
                    interactionSource = interactionSource
                ) {
                    if (state().settings.general.displayRotationVertical) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize().padding(16.dp).pressEmphasis()
                        ) {
                            Text(
                                "Calls",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate(180f)
                            )
                            HorizontalDivider()
                            Text(
                                "Trump",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize().padding(16.dp).pressEmphasis()
                        ) {
                            Text(
                                "Calls",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90()
                            )
                            VerticalDivider()
                            Text(
                                "Trump",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90(negative = true)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentRotationPicker(state: AppState.Prop) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text("Content rotation")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
        ) {
            PressableWithEmphasis {
                PickerCard(
                    onClick = {
                        state {
                            AppState.settings.general.contentRotation set
                                ContentRotationSetting.Auto
                        }
                    },
                    selected =
                        state().settings.general.contentRotation == ContentRotationSetting.Auto,
                    interactionSource = interactionSource
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            "Auto switch",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp).pressEmphasis()
                        )
                    }
                }
            }
            PressableWithEmphasis {
                PickerCard(
                    onClick = {
                        state {
                            AppState.settings.general.contentRotation set
                                ContentRotationSetting.Center
                        }
                    },
                    selected =
                        state().settings.general.contentRotation == ContentRotationSetting.Center,
                    interactionSource = interactionSource
                ) {
                    if (state().settings.general.displayRotationVertical) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize().padding(16.dp).pressEmphasis()
                        ) {
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate(180f)
                            )
                            HorizontalDivider()
                            Text("Aa", textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 32.dp)
                                    .pressEmphasis()
                        ) {
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90()
                            )
                            VerticalDivider()
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90(negative = true)
                            )
                        }
                    }
                }
            }
            PressableWithEmphasis {
                PickerCard(
                    onClick = {
                        state {
                            AppState.settings.general.contentRotation set
                                ContentRotationSetting.TopTowardsRight
                        }
                    },
                    selected =
                        state().settings.general.contentRotation ==
                            ContentRotationSetting.TopTowardsRight,
                    interactionSource = interactionSource
                ) {
                    if (state().settings.general.displayRotationVertical) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize().padding(16.dp).pressEmphasis()
                        ) {
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90(negative = true)
                            )
                            HorizontalDivider()
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90()
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 32.dp)
                                    .pressEmphasis()
                        ) {
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate(180f)
                            )
                            VerticalDivider()
                            Text("Aa", textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            PressableWithEmphasis {
                PickerCard(
                    onClick = {
                        state {
                            AppState.settings.general.contentRotation set
                                ContentRotationSetting.BottomTowardsRight
                        }
                    },
                    selected =
                        state().settings.general.contentRotation ==
                            ContentRotationSetting.BottomTowardsRight,
                    interactionSource = interactionSource
                ) {
                    if (state().settings.general.displayRotationVertical) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize().padding(16.dp).pressEmphasis()
                        ) {
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90()
                            )
                            HorizontalDivider()
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate90(negative = true)
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 32.dp)
                                    .pressEmphasis()
                        ) {
                            Text("Aa", textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            VerticalDivider()
                            Text(
                                "Aa",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f).rotate(180f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private val autoSwitchIntervals =
    mapOf(
        3 to "3 seconds",
        5 to "5 seconds",
        10 to "10 seconds",
        20 to "20 seconds",
        60 to "1 minute",
        300 to "5 minutes",
        1_577_847_600 to "50 years"
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AutoSwitchSecondsPicker(
    autoSwitchSeconds: Int,
    setAutoSwitchSeconds: (Int) -> Unit,
    enabled: Boolean
) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            "Auto switch interval",
            color = LocalContentColor.current.copy(alpha = if (enabled) 1f else 0.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        var expanded by rememberSaveable { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
        ) {
            OutlinedTextField(
                value = autoSwitchIntervals[autoSwitchSeconds] ?: "$autoSwitchSeconds seconds",
                onValueChange = {},
                enabled = enabled,
                readOnly = true,
                leadingIcon = { Icon(iconRes(Res.drawable.ic_timer), null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    focusManager.clearFocus()
                },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            ) {
                autoSwitchIntervals.forEach { (seconds, name) ->
                    DropdownMenuItem(
                        text = { Text(name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        onClick = {
                            setAutoSwitchSeconds(seconds)
                            expanded = false
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    }
}
