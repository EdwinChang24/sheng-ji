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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import interaction.PressableWithEmphasis
import model.AppState
import model.settings
import settings.ContentRotationSetting
import settings.contentRotation
import settings.general
import util.ExpandHeights
import util.ExpandWidths
import util.ExpandWidthsScope
import util.WeightColumn
import util.WeightRow
import util.rotate90

@Composable
fun ExpandWidthsScope.SettingsContentRotationPicker(state: AppState.Prop) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text("Content rotation", modifier = Modifier.padding(horizontal = 24.dp))
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
                                    AppState.settings.general.contentRotation set
                                        ContentRotationSetting.Auto
                                }
                            },
                            selected =
                                state().settings.general.contentRotation ==
                                    ContentRotationSetting.Auto,
                            interactionSource = interactionSource,
                            modifier = Modifier.weight()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.expandWidth().expandHeight().pressEmphasis()
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
                                    AppState.settings.general.contentRotation set
                                        ContentRotationSetting.Center
                                }
                            },
                            selected =
                                state().settings.general.contentRotation ==
                                    ContentRotationSetting.Center,
                            interactionSource = interactionSource,
                            modifier = Modifier.weight()
                        ) {
                            if (state().settings.general.displayRotationVertical) {
                                WeightColumn(
                                    spacing = 8.dp,
                                    modifier =
                                        Modifier.expandHeight().pressEmphasis().padding(16.dp)
                                ) {
                                    Text(
                                        "Aa",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Visible,
                                        modifier = Modifier.weight().expandWidth().rotate(180f)
                                    )
                                    HorizontalDivider(modifier = Modifier.expandWidth())
                                    Text(
                                        "Aa",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Visible,
                                        modifier = Modifier.weight().expandWidth()
                                    )
                                }
                            } else {
                                WeightRow(
                                    spacing = 8.dp,
                                    modifier =
                                        Modifier.expandHeight().pressEmphasis().padding(16.dp)
                                ) {
                                    Text(
                                        "Aa",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Visible,
                                        modifier = Modifier.weight().expandHeight().rotate90()
                                    )
                                    VerticalDivider(modifier = Modifier.expandHeight())
                                    Text(
                                        "Aa",
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Visible,
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
                                    AppState.settings.general.contentRotation set
                                        ContentRotationSetting.TopTowardsRight
                                }
                            },
                            selected =
                                state().settings.general.contentRotation ==
                                    ContentRotationSetting.TopTowardsRight,
                            interactionSource = interactionSource,
                            modifier = Modifier.weight()
                        ) {
                            if (state().settings.general.displayRotationVertical) {
                                WeightColumn(
                                    spacing = 8.dp,
                                    modifier =
                                        Modifier.expandHeight().pressEmphasis().padding(16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandWidth()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.rotate90(negative = true)
                                        )
                                    }
                                    HorizontalDivider(modifier = Modifier.expandWidth())
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandWidth()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.rotate90()
                                        )
                                    }
                                }
                            } else {
                                WeightRow(
                                    spacing = 8.dp,
                                    modifier =
                                        Modifier.expandHeight().pressEmphasis().padding(16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandHeight()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.rotate(180f)
                                        )
                                    }
                                    VerticalDivider(modifier = Modifier.expandHeight())
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandHeight()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible
                                        )
                                    }
                                }
                            }
                        }
                    }
                    PressableWithEmphasis {
                        SettingsPickerCard(
                            onClick = {
                                state {
                                    AppState.settings.general.contentRotation set
                                        ContentRotationSetting.BottomTowardsRight
                                }
                            },
                            selected =
                                state().settings.general.contentRotation ==
                                    ContentRotationSetting.BottomTowardsRight,
                            interactionSource = interactionSource,
                            modifier = Modifier.weight()
                        ) {
                            if (state().settings.general.displayRotationVertical) {
                                WeightColumn(
                                    spacing = 8.dp,
                                    modifier =
                                        Modifier.expandHeight().pressEmphasis().padding(16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandWidth()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.rotate90()
                                        )
                                    }
                                    HorizontalDivider(modifier = Modifier.expandWidth())
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandWidth()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.rotate90(negative = true)
                                        )
                                    }
                                }
                            } else {
                                WeightRow(
                                    spacing = 8.dp,
                                    modifier =
                                        Modifier.expandHeight().pressEmphasis().padding(16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandHeight()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible
                                        )
                                    }
                                    VerticalDivider(modifier = Modifier.expandHeight())
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.weight().expandHeight()
                                    ) {
                                        Text(
                                            "Aa",
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.rotate(180f)
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
}
