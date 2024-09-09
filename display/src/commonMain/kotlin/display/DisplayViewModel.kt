package display

import androidx.lifecycle.ViewModel
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import settings.Settings

class DisplayViewModel : ViewModel() {
    private var job: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var possibleContentPairs = listOf<DisplayContentPair>()
    private var possibleRotations = listOf<ContentRotation>()
    private var pause = false
    private var autoSwitchSeconds = Settings().general.autoSwitchSeconds
    val currentContent =
        MutableStateFlow(DisplayContent.None and DisplayContent.None with ContentRotation.Center)

    suspend fun onStateUpdate(
        newPossibleContentPairs: List<DisplayContentPair>,
        newPossibleRotations: List<ContentRotation>,
        newPause: Boolean,
        newAutoSwitchSeconds: Int,
    ) {
        possibleContentPairs = newPossibleContentPairs
        possibleRotations = newPossibleRotations
        pause = newPause
        autoSwitchSeconds = newAutoSwitchSeconds
        if (
            currentContent.value.displayContentPair !in possibleContentPairs ||
                currentContent.value.rotation !in possibleRotations ||
                job?.isActive != true
        ) {
            job?.cancelAndJoin()
            job = coroutineScope.launch { update() }
        }
    }

    private suspend fun update() {
        if (possibleContentPairs.size > 1 || possibleRotations.size > 1) {
            while (true) {
                while (pause) yield()
                currentContent.value =
                    getNextContent(possibleContentPairs, possibleRotations, currentContent.value)
                delay(autoSwitchSeconds.seconds)
            }
        } else {
            currentContent.value =
                getNextContent(possibleContentPairs, possibleRotations, currentContent.value)
        }
    }

    private fun getNextContent(
        possibleContentPairs: List<DisplayContentPair>,
        possibleRotations: List<ContentRotation>,
        currentContent: DisplayContentWithRotation,
    ): DisplayContentWithRotation {
        val newRotation =
            possibleRotations.getOrNull(
                (possibleRotations.indexOf(currentContent.rotation) + 1) % possibleRotations.size
            ) ?: possibleRotations.firstOrNull() ?: ContentRotation.Center
        val newContent =
            (if (currentContent.displayContentPair !in possibleContentPairs) {
                null
            } else if (currentContent.rotation == possibleRotations.lastOrNull()) {
                possibleContentPairs.getOrNull(
                    (possibleContentPairs.indexOf(currentContent.displayContentPair) -
                        (possibleRotations.size - 2) +
                        ((possibleRotations.size / possibleContentPairs.size + 1) *
                            possibleContentPairs.size)) % possibleContentPairs.size
                )
            } else {
                possibleContentPairs.getOrNull(
                    (possibleContentPairs.indexOf(currentContent.displayContentPair) + 1) %
                        possibleContentPairs.size
                )
            })
                ?: possibleContentPairs.firstOrNull()
                ?: (DisplayContent.None and DisplayContent.None)
        return newContent with newRotation
    }
}
