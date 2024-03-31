package io.github.edwinchang24.shengjidisplay.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.edwinchang24.shengjidisplay.model.HorizontalOrientation
import io.github.edwinchang24.shengjidisplay.model.VerticalOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DisplayViewModel @Inject constructor() : ViewModel() {
    private var job: Job? = null
    private var displaySettingsState: DisplaySettingsState? = null
    val autoPlay = MutableStateFlow(true)

    val topContent = MutableStateFlow<DisplayContent>(DisplayContent.None)
    val bottomContent = MutableStateFlow<DisplayContent>(DisplayContent.None)

    suspend fun onPotentialUpdate(settingsState: DisplaySettingsState) {
        if (displaySettingsState != settingsState) {
            job?.cancelAndJoin()
            displaySettingsState = settingsState
            job = viewModelScope.launch { update() }
        }
    }

    private suspend fun update() {
        displaySettingsState?.run {
            val topDirection = if (perpendicularMode) when (horizontalOrientation) {
                HorizontalOrientation.Auto, HorizontalOrientation.TopTowardsRight -> DisplayContent.Direction.Right
                HorizontalOrientation.BottomTowardsRight -> DisplayContent.Direction.Left
            } else DisplayContent.Direction.Center
            topContent.value = if (!showCalls) DisplayContent.Trump(topDirection) else when (verticalOrder) {
                VerticalOrder.Auto, VerticalOrder.TrumpOnTop -> DisplayContent.Trump(topDirection)
                VerticalOrder.CallsOnTop -> DisplayContent.Calls(topDirection)
            }
            val bottomDirection = if (perpendicularMode) when (horizontalOrientation) {
                HorizontalOrientation.Auto, HorizontalOrientation.TopTowardsRight -> DisplayContent.Direction.Left
                HorizontalOrientation.BottomTowardsRight -> DisplayContent.Direction.Right
            } else DisplayContent.Direction.Center
            bottomContent.value = if (!showCalls) DisplayContent.Trump(bottomDirection) else when (verticalOrder) {
                VerticalOrder.Auto, VerticalOrder.TrumpOnTop -> DisplayContent.Calls(bottomDirection)
                VerticalOrder.CallsOnTop -> DisplayContent.Trump(bottomDirection)
            }
            if (verticalOrder == VerticalOrder.Auto || (perpendicularMode && horizontalOrientation == HorizontalOrientation.Auto)) {
                while (true) {
                    delay(autoSwitchSeconds.seconds)
                    while (!autoPlay.value) yield()
                    if (showCalls) when {
                        verticalOrder == VerticalOrder.Auto && horizontalOrientation == HorizontalOrientation.Auto -> {
                            when (val tc = topContent.value) {
                                is DisplayContent.Trump -> when (tc.direction) {
                                    DisplayContent.Direction.Center -> {
                                        topContent.value = DisplayContent.Calls(DisplayContent.Direction.Center)
                                        bottomContent.value = DisplayContent.Trump(DisplayContent.Direction.Center)
                                    }

                                    DisplayContent.Direction.Left -> {
                                        topContent.value = DisplayContent.Calls(DisplayContent.Direction.Right)
                                        bottomContent.value = DisplayContent.Trump(DisplayContent.Direction.Left)
                                    }

                                    DisplayContent.Direction.Right -> {
                                        topContent.value = DisplayContent.Calls(DisplayContent.Direction.Left)
                                        bottomContent.value = DisplayContent.Trump(DisplayContent.Direction.Right)
                                    }
                                }

                                is DisplayContent.Calls -> when (tc.direction) {
                                    DisplayContent.Direction.Center -> {
                                        topContent.value = DisplayContent.Trump(DisplayContent.Direction.Center)
                                        bottomContent.value = DisplayContent.Calls(DisplayContent.Direction.Center)
                                    }

                                    DisplayContent.Direction.Left -> {
                                        topContent.value = DisplayContent.Trump(DisplayContent.Direction.Left)
                                        bottomContent.value = DisplayContent.Calls(DisplayContent.Direction.Right)
                                    }

                                    DisplayContent.Direction.Right -> {
                                        topContent.value = DisplayContent.Trump(DisplayContent.Direction.Right)
                                        bottomContent.value = DisplayContent.Calls(DisplayContent.Direction.Left)
                                    }
                                }

                                DisplayContent.None -> error("Should've initialized topContent.value")
                            }
                        }

                        verticalOrder == VerticalOrder.Auto -> {
                            when (val tc = topContent.value) {
                                is DisplayContent.Trump -> {
                                    topContent.value = DisplayContent.Calls(tc.direction)
                                    bottomContent.value = DisplayContent.Trump(tc.direction.opposite())
                                }

                                is DisplayContent.Calls -> {
                                    topContent.value = DisplayContent.Trump(tc.direction)
                                    bottomContent.value = DisplayContent.Calls(tc.direction.opposite())
                                }

                                DisplayContent.None -> error("Should've initialized topContent.value")
                            }
                        }

                        horizontalOrientation == HorizontalOrientation.Auto -> {
                            when (val tc = topContent.value) {
                                is DisplayContent.Trump -> {
                                    topContent.value = DisplayContent.Trump(tc.direction.opposite())
                                    bottomContent.value = DisplayContent.Calls(tc.direction)
                                }

                                is DisplayContent.Calls -> {
                                    topContent.value = DisplayContent.Calls(tc.direction.opposite())
                                    bottomContent.value = DisplayContent.Trump(tc.direction)
                                }

                                DisplayContent.None -> error("Should've initialized topContent.value")
                            }
                        }
                    } else {
                        val tc = topContent.value as? DisplayContent.Trump ?: error("topContent.value should be Trump")
                        if (horizontalOrientation == HorizontalOrientation.Auto) {
                            topContent.value = DisplayContent.Trump(tc.direction.opposite())
                            bottomContent.value = DisplayContent.Trump(tc.direction)
                        } else {
                            topContent.value = DisplayContent.Trump(tc.direction)
                            bottomContent.value = DisplayContent.Trump(tc.direction.opposite())
                        }
                    }
                }
            }
        }
    }
}
