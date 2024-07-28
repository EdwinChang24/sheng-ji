package settings.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import interaction.PressableWithEmphasis
import model.AppState
import model.settings
import settings.MainDisplayOrder
import settings.displayOrder
import settings.mainDisplay
import util.ExpandHeights
import util.ExpandWidths
import util.ExpandWidthsScope
import util.WeightColumn
import util.WeightRow
import util.rotate90

@Composable
fun ExpandWidthsScope.SettingsMainDisplayOrderPicker(state: AppState.Prop) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text("Display order", modifier = Modifier.padding(horizontal = 24.dp))
        ExpandHeights(
            modifier =
                Modifier.expandWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
        ) {
            ExpandWidths {
                WeightRow(spacing = 8.dp) {
                    PressableWithEmphasis {
                        SettingsPickerCard(
                            onClick = {
                                state {
                                    AppState.settings.mainDisplay.displayOrder set
                                        MainDisplayOrder.Auto
                                }
                            },
                            selected =
                                state().settings.mainDisplay.displayOrder == MainDisplayOrder.Auto,
                            interactionSource = interactionSource,
                            modifier = Modifier.weight()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.expandHeight().expandWidth().pressEmphasis()
                            ) {
                                Text(
                                    "Auto switch",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                    PressableWithEmphasis {
                        SettingsPickerCard(
                            onClick = {
                                state {
                                    AppState.settings.mainDisplay.displayOrder set
                                        MainDisplayOrder.TrumpOnTop
                                }
                            },
                            selected =
                                state().settings.mainDisplay.displayOrder ==
                                    MainDisplayOrder.TrumpOnTop,
                            interactionSource = interactionSource,
                            modifier = Modifier.weight()
                        ) {
                            if (state().settings.general.displayRotationVertical) {
                                WeightColumn(
                                    spacing = 8.dp,
                                    modifier = Modifier.pressEmphasis().padding(16.dp)
                                ) {
                                    Text(
                                        "Trump",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight().expandWidth().rotate(180f)
                                    )
                                    HorizontalDivider(modifier = Modifier.expandWidth())
                                    Text(
                                        "Calls",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight().expandWidth()
                                    )
                                }
                            } else {
                                WeightRow(
                                    spacing = 8.dp,
                                    modifier = Modifier.pressEmphasis().padding(16.dp)
                                ) {
                                    Text(
                                        "Trump",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight().expandHeight().rotate90()
                                    )
                                    VerticalDivider(modifier = Modifier.expandHeight())
                                    Text(
                                        "Calls",
                                        textAlign = TextAlign.Center,
                                        modifier =
                                            Modifier.weight()
                                                .expandHeight()
                                                .rotate90(negative = true)
                                    )
                                }
                            }
                        }
                    }
                    PressableWithEmphasis {
                        SettingsPickerCard(
                            onClick = {
                                state {
                                    AppState.settings.mainDisplay.displayOrder set
                                        MainDisplayOrder.CallsOnTop
                                }
                            },
                            selected =
                                state().settings.mainDisplay.displayOrder ==
                                    MainDisplayOrder.CallsOnTop,
                            interactionSource = interactionSource,
                            modifier = Modifier.weight()
                        ) {
                            if (state().settings.general.displayRotationVertical) {
                                WeightColumn(
                                    spacing = 8.dp,
                                    modifier = Modifier.pressEmphasis().padding(16.dp)
                                ) {
                                    Text(
                                        "Calls",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight().expandWidth().rotate(180f)
                                    )
                                    HorizontalDivider(modifier = Modifier.expandWidth())
                                    Text(
                                        "Trump",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight().expandWidth()
                                    )
                                }
                            } else {
                                WeightRow(
                                    spacing = 8.dp,
                                    modifier = Modifier.pressEmphasis().padding(16.dp)
                                ) {
                                    Text(
                                        "Calls",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight().expandHeight().rotate90()
                                    )
                                    VerticalDivider(modifier = Modifier.expandHeight())
                                    Text(
                                        "Trump",
                                        textAlign = TextAlign.Center,
                                        modifier =
                                            Modifier.weight()
                                                .expandHeight()
                                                .rotate90(negative = true)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
