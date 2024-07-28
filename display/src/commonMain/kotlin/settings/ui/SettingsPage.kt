package settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.IconButtonWithEmphasis
import model.AppState
import model.settings
import navigation.Navigator
import resources.Res
import resources.ic_arrow_back
import settings.ContentRotationSetting
import settings.MainDisplayOrder
import settings.autoHideCalls
import settings.autoSwitchSeconds
import settings.general
import settings.mainDisplay
import settings.possibleTrumpsDisplay
import settings.showClock
import settings.tapToEdit
import settings.tapTrumpToEdit
import settings.theme
import settings.underline6And9
import util.ExpandWidths
import util.iconRes

@Composable
fun SettingsPage(navigator: Navigator, state: AppState.Prop, modifier: Modifier = Modifier) {
    ExpandWidths {
        Column(
            modifier =
                modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.expandWidth().padding(12.dp)
            ) {
                IconButtonWithEmphasis(onClick = { navigator.toggleSettings() }) {
                    Icon(iconRes(Res.drawable.ic_arrow_back), null)
                }
                Text("Display settings", style = MaterialTheme.typography.titleLarge)
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                SettingsSectionHeader("General")
                SettingsThemePicker(
                    theme = state().settings.general.theme,
                    setTheme = { state { AppState.settings.general.theme set it } }
                )
                SettingsContentRotationPicker(state)
                SettingsAutoSwitchSecondsPicker(
                    autoSwitchSeconds = state().settings.general.autoSwitchSeconds,
                    setAutoSwitchSeconds = {
                        state { AppState.settings.general.autoSwitchSeconds set it }
                    },
                    enabled =
                        state().settings.mainDisplay.displayOrder == MainDisplayOrder.Auto ||
                            state().settings.general.contentRotation == ContentRotationSetting.Auto
                )
                SettingsBooleanPicker(
                    value = state().settings.general.showClock,
                    setValue = { state { AppState.settings.general.showClock set it } }
                ) {
                    Text("Show clock")
                }
                SettingsBooleanPicker(
                    value = state().settings.general.underline6And9,
                    setValue = { state { AppState.settings.general.underline6And9 set it } }
                ) {
                    Text("Underline 6s and 9s")
                }
                SettingsSectionHeader("Main display")
                SettingsMainDisplayOrderPicker(state)
                SettingsBooleanPicker(
                    value = state().settings.mainDisplay.autoHideCalls,
                    setValue = { state { AppState.settings.mainDisplay.autoHideCalls set it } }
                ) {
                    Text("Hide calls when all are found")
                }
                SettingsBooleanPicker(
                    value = state().settings.mainDisplay.tapTrumpToEdit,
                    setValue = { state { AppState.settings.mainDisplay.tapTrumpToEdit set it } }
                ) {
                    Text("Tap trump card in display to edit")
                }
                SettingsSectionHeader("Possible trumps display")
                SettingsBooleanPicker(
                    value = state().settings.possibleTrumpsDisplay.tapToEdit,
                    setValue = {
                        state { AppState.settings.possibleTrumpsDisplay.tapToEdit set it }
                    }
                ) {
                    Text("Tap display to edit")
                }
                PlatformSettingsSection(
                    state,
                    { text, modifier1 -> SettingsSectionHeader(text, modifier1) },
                    { value, setValue, label -> SettingsBooleanPicker(value, setValue, label) }
                )
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}
