package io.github.edwinchang24.shengjidisplay.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.edwinchang24.shengjidisplay.model.HorizontalOrientation
import io.github.edwinchang24.shengjidisplay.model.VerticalOrder
import io.github.edwinchang24.shengjidisplay.pages.DisplayContent.Calls
import io.github.edwinchang24.shengjidisplay.pages.DisplayContent.Direction
import io.github.edwinchang24.shengjidisplay.pages.DisplayContent.None
import io.github.edwinchang24.shengjidisplay.pages.DisplayContent.Trump
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@HiltViewModel
class DisplayViewModel @Inject constructor() : ViewModel() {
    private var job: Job? = null
    private var displaySettingsState: DisplaySettingsState? = null
    val autoPlay = MutableStateFlow(true)

    val topContent = MutableStateFlow<DisplayContent>(None)
    val bottomContent = MutableStateFlow<DisplayContent>(None)

    suspend fun onPotentialUpdate(settingsState: DisplaySettingsState) {
        if (displaySettingsState != settingsState) {
            job?.cancelAndJoin()
            displaySettingsState = settingsState
            job = viewModelScope.launch { update() }
        }
    }

    private suspend fun update() {
        displaySettingsState?.run {
            val topDirection =
                if (perpendicularMode) {
                    when (horizontalOrientation) {
                        HorizontalOrientation.Auto,
                        HorizontalOrientation.TopTowardsRight -> Direction.Right
                        HorizontalOrientation.BottomTowardsRight -> Direction.Left
                    }
                } else Direction.Center
            topContent.value =
                if (!showCalls) Trump(topDirection)
                else {
                    when (verticalOrder) {
                        VerticalOrder.Auto,
                        VerticalOrder.TrumpOnTop -> Trump(topDirection)
                        VerticalOrder.CallsOnTop -> Calls(topDirection)
                    }
                }
            val bottomDirection =
                if (perpendicularMode) {
                    when (horizontalOrientation) {
                        HorizontalOrientation.Auto,
                        HorizontalOrientation.TopTowardsRight -> Direction.Left
                        HorizontalOrientation.BottomTowardsRight -> Direction.Right
                    }
                } else Direction.Center
            bottomContent.value =
                if (!showCalls) Trump(bottomDirection)
                else {
                    when (verticalOrder) {
                        VerticalOrder.Auto,
                        VerticalOrder.TrumpOnTop -> Calls(bottomDirection)
                        VerticalOrder.CallsOnTop -> Trump(bottomDirection)
                    }
                }
            if (
                verticalOrder == VerticalOrder.Auto ||
                    (perpendicularMode && horizontalOrientation == HorizontalOrientation.Auto)
            ) {
                while (true) {
                    delay(autoSwitchSeconds.seconds)
                    while (!autoPlay.value) yield()
                    if (showCalls) {
                        when {
                            verticalOrder == VerticalOrder.Auto &&
                                horizontalOrientation == HorizontalOrientation.Auto -> {
                                when (val tc = topContent.value) {
                                    is Trump ->
                                        when (tc.direction) {
                                            Direction.Center -> {
                                                topContent.value = Calls(Direction.Center)
                                                bottomContent.value = Trump(Direction.Center)
                                            }
                                            Direction.Left -> {
                                                topContent.value = Calls(Direction.Right)
                                                bottomContent.value = Trump(Direction.Left)
                                            }
                                            Direction.Right -> {
                                                topContent.value = Calls(Direction.Left)
                                                bottomContent.value = Trump(Direction.Right)
                                            }
                                        }
                                    is Calls ->
                                        when (tc.direction) {
                                            Direction.Center -> {
                                                topContent.value = Trump(Direction.Center)
                                                bottomContent.value = Calls(Direction.Center)
                                            }
                                            Direction.Left -> {
                                                topContent.value = Trump(Direction.Left)
                                                bottomContent.value = Calls(Direction.Right)
                                            }
                                            Direction.Right -> {
                                                topContent.value = Trump(Direction.Right)
                                                bottomContent.value = Calls(Direction.Left)
                                            }
                                        }
                                    None -> error("Should've initialized topContent.value")
                                }
                            }
                            verticalOrder == VerticalOrder.Auto -> {
                                when (val tc = topContent.value) {
                                    is Trump -> {
                                        topContent.value = Calls(tc.direction)
                                        bottomContent.value = Trump(tc.direction.opposite())
                                    }
                                    is Calls -> {
                                        topContent.value = Trump(tc.direction)
                                        bottomContent.value = Calls(tc.direction.opposite())
                                    }
                                    None -> error("Should've initialized topContent.value")
                                }
                            }
                            horizontalOrientation == HorizontalOrientation.Auto -> {
                                when (val tc = topContent.value) {
                                    is Trump -> {
                                        topContent.value = Trump(tc.direction.opposite())
                                        bottomContent.value = Calls(tc.direction)
                                    }
                                    is Calls -> {
                                        topContent.value = Calls(tc.direction.opposite())
                                        bottomContent.value = Trump(tc.direction)
                                    }
                                    None -> error("Should've initialized topContent.value")
                                }
                            }
                        }
                    } else {
                        val tc =
                            topContent.value as? Trump ?: error("topContent.value should be Trump")
                        if (horizontalOrientation == HorizontalOrientation.Auto) {
                            topContent.value = Trump(tc.direction.opposite())
                            bottomContent.value = Trump(tc.direction)
                        } else {
                            topContent.value = Trump(tc.direction)
                            bottomContent.value = Trump(tc.direction.opposite())
                        }
                    }
                }
            }
        }
    }
}
