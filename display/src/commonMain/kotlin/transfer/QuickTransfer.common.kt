package transfer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import components.ButtonWithEmphasis
import components.CallFoundText
import components.IconButtonWithEmphasis
import components.OutlinedButtonWithEmphasis
import components.PlayingCard
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import model.AppState
import model.calls
import model.possibleTrumps
import model.teammates
import model.trump
import navigation.Navigator
import resources.Res
import resources.ic_close
import resources.ic_content_copy
import resources.ic_content_paste
import resources.ic_done
import resources.ic_input
import resources.ic_link
import resources.ic_output
import util.DefaultTransition
import util.ExpandWidths
import util.ExpandWidthsScope
import util.WindowWidth
import util.calculateWindowWidth
import util.iconRes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun QuickTransferDialog(
    url: String? = null,
    navigator: Navigator,
    state: AppState.Prop,
    modifier: Modifier = Modifier,
) {
    val windowWidth = calculateWindowWidth()
    val data =
        TransferredData(
                state().possibleTrumps,
                state().trump,
                state().calls,
                state().teammates.values.toList(),
            )
            .toUrl()
    var importUrl by rememberSaveable { mutableStateOf(url ?: "") }
    val dataFromImport = dataParamFromUrl(importUrl)?.let { decodeParam(it) }
    val importFieldFocusRequester = FocusRequester()
    ExpandWidths {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.verticalScroll(rememberScrollState()).padding(24.dp),
        ) {
            Text(
                "Quick transfer",
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            var exportTab by rememberSaveable { mutableStateOf(url == null) }
            Column(
                modifier =
                    Modifier.expandWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                LaunchedEffect(exportTab) {
                    if (!exportTab) importFieldFocusRequester.requestFocus()
                }
                PrimaryTabRow(
                    selectedTabIndex = if (exportTab) 0 else 1,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).expandWidth().width(0.dp),
                ) {
                    Tab(
                        selected = exportTab,
                        onClick = { exportTab = true },
                        text = { Text("Export", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        icon = { Icon(iconRes(Res.drawable.ic_output), null) },
                    )
                    Tab(
                        selected = !exportTab,
                        onClick = { exportTab = false },
                        text = { Text("Import", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        icon = { Icon(iconRes(Res.drawable.ic_input), null) },
                    )
                }
                AnimatedContent(
                    targetState = exportTab,
                    transitionSpec = {
                        slideInHorizontally { if (targetState) -it else it } togetherWith
                            slideOutHorizontally { if (targetState) it else -it }
                    },
                    modifier = Modifier.expandWidth(),
                ) { targetState ->
                    if (targetState) Export(data, state, windowWidth)
                    else
                        Import(
                            importUrl,
                            { importUrl = it },
                            dataFromImport,
                            focusRequester = importFieldFocusRequester,
                            state,
                        )
                }
            }
            ButtonWithEmphasis(
                text = if (exportTab) "Done" else "Import",
                icon =
                    if (exportTab) iconRes(Res.drawable.ic_done)
                    else iconRes(Res.drawable.ic_input),
                onClick = {
                    if (!exportTab && dataFromImport != null && !dataFromImport.isEmpty()) {
                        state {
                            dataFromImport.possibleTrumps?.let { AppState.possibleTrumps set it }
                            dataFromImport.trump?.let { AppState.trump set it }
                            dataFromImport.calls?.let { AppState.calls set it }
                            dataFromImport.teammates?.let {
                                AppState.teammates set it.associateBy { Uuid.random().toString() }
                            }
                        }
                    }
                    navigator.closeDialog()
                },
                enabled = exportTab || (dataFromImport != null && !dataFromImport.isEmpty()),
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Composable
private fun Export(
    data: String,
    state: AppState.Prop,
    windowWidth: WindowWidth,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp),
    ) {
        QrImage(
            data,
            state,
            modifier =
                Modifier.size(if (windowWidth > WindowWidth.Small) 300.dp else 200.dp)
                    .aspectRatio(1f),
        )
        var copied: Boolean by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(data) { copied = false }
        val clipboardManager = LocalClipboardManager.current
        OutlinedButtonWithEmphasis(
            text =
                if (copied) "Copied!"
                else if (windowWidth <= WindowWidth.ExtraSmall) "Copy URL"
                else "Copy URL to clipboard",
            icon =
                if (copied) iconRes(Res.drawable.ic_done)
                else iconRes(Res.drawable.ic_content_copy),
            onClick = {
                clipboardManager.setText(buildAnnotatedString { append(data) })
                copied = true
            },
        )
        Text(
            "Data embedded in URL. Settings not included.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Composable
private fun ExpandWidthsScope.Import(
    importUrl: String,
    setImportUrl: (String) -> Unit,
    dataFromImport: TransferredData?,
    focusRequester: FocusRequester,
    state: AppState.Prop,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(24.dp)) {
        OutlinedTextField(
            importUrl,
            setImportUrl,
            label = { Text("Import from URL", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            placeholder = {
                Text(
                    "$BaseUrl...",
                    color = LocalContentColor.current.copy(alpha = 0.25f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingIcon = { Icon(iconRes(Res.drawable.ic_link), null) },
            trailingIcon = {
                if (importUrl.isNotEmpty()) {
                    IconButtonWithEmphasis(
                        onClick = {
                            setImportUrl("")
                            focusRequester.requestFocus()
                        }
                    ) {
                        Icon(iconRes(Res.drawable.ic_close), null)
                    }
                }
            },
            singleLine = true,
            modifier =
                Modifier.expandWidth()
                    .widthIn(0.dp, WindowWidth.Medium.breakpoint)
                    .focusRequester(focusRequester)
                    .animateContentSize(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        val clipboardManager = LocalClipboardManager.current
        var clipboardHasText by remember {
            mutableStateOf(!clipboardManager.getText().isNullOrEmpty())
        }
        SideEffect { clipboardHasText = !clipboardManager.getText().isNullOrEmpty() }
        OutlinedButtonWithEmphasis(
            text = "From clipboard",
            icon = iconRes(Res.drawable.ic_content_paste),
            onClick = { clipboardManager.getText()?.text?.let(setImportUrl) },
            enabled = clipboardHasText,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedContent(
            targetState = dataFromImport?.isEmpty()?.not() ?: false,
            transitionSpec = {
                fadeIn() + slideInVertically() togetherWith
                    fadeOut() + slideOutVertically() using
                    SizeTransform(clip = false)
            },
        ) { visible ->
            Column(modifier = Modifier.expandWidth()) {
                if (visible) {
                    Text(
                        "To be imported",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AnimatedContent(
                        targetState = dataFromImport,
                        transitionSpec = { DefaultTransition using SizeTransform(clip = false) },
                        modifier = Modifier.expandWidth(),
                    ) { targetState ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.expandWidth(),
                        ) {
                            targetState?.let { data ->
                                data.possibleTrumps
                                    ?.takeIf { it.isNotEmpty() }
                                    ?.let { possibleTrumps ->
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                "Possible trumps",
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                            Text(
                                                possibleTrumps.joinToString(),
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        }
                                    }
                                data.trump?.let { trump ->
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            "Trump card",
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        PlayingCard(trump, state)
                                    }
                                }
                                data.calls
                                    ?.takeIf { it.isNotEmpty() }
                                    ?.let { calls ->
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                "Calls",
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                calls.forEach { call ->
                                                    Column(
                                                        horizontalAlignment =
                                                            Alignment.CenterHorizontally
                                                    ) {
                                                        PlayingCard(call.card, state)
                                                        CallFoundText(
                                                            call,
                                                            style =
                                                                MaterialTheme.typography.bodySmall,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                data.teammates
                                    ?.takeIf { it.isNotEmpty() }
                                    ?.let { teammates ->
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                "Teammates",
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                            Text(
                                                "${teammates.size} teammate${if (teammates.size == 1) "" else "s"}",
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
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

@Composable expect fun QrImage(data: String, state: AppState.Prop, modifier: Modifier = Modifier)
