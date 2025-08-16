package com.example.anothersmartvoicejournal.feature.journal.ui.journal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anothersmartvoicejournal.core.common.mapper.toUiModel
import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.ui.components.EmptyState
import com.example.anothersmartvoicejournal.core.ui.components.LoadingIndicator
import com.example.anothersmartvoicejournal.core.ui.components.TopAppBar
import com.example.anothersmartvoicejournal.core.ui.model.EmptyStateUiModel
import com.example.anothersmartvoicejournal.core.ui.model.LoadingIndicatorUiModel
import com.example.anothersmartvoicejournal.core.ui.model.LoadingState
import com.example.anothersmartvoicejournal.core.ui.model.LoadingType
import com.example.anothersmartvoicejournal.core.ui.model.TopAppBarUiModel
import com.example.anothersmartvoicejournal.feature.journal.ui.components.JournalEntryCard

@Composable
fun JournalScreen(
    onNavigateToRecording: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                uiModel = TopAppBarUiModel(
                    title = "Journal",
                    showBackButton = false
                ),
                modifier = modifier
            )
        },
        floatingActionButton = { StartRecordingButton(onNavigateToRecording = onNavigateToRecording) },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        content = { innerPadding ->
            JournalContent(
                uiState = uiState,
                modifier = Modifier.padding(innerPadding),
                onStartRecording = onNavigateToRecording
            )
        }
    )
}

@Composable
fun JournalContent(
    uiState: JournalUiState,
    modifier: Modifier = Modifier,
    onStartRecording: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                LoadingIndicator(
                    uiModel = LoadingIndicatorUiModel(
                        state = LoadingState.LOADING,
                        type = LoadingType.CIRCULAR,
                        message = "Loading your journal entries..."
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }
            uiState.error != null -> {
                LoadingIndicator(
                    uiModel = LoadingIndicatorUiModel(
                        state = LoadingState.ERROR,
                        type = LoadingType.CIRCULAR,
                        message = uiState.error
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }
            uiState.journalEntries.isEmpty() -> {
                EmptyState(
                    uiModel = EmptyStateUiModel(
                        title = "No Journal Entries",
                        message = "Start your voice journaling journey by recording your first entry.",
                        icon = Icons.Default.Mic,
                        actionText = "Start Recording",
                        onActionClick = onStartRecording
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                JournalEntriesList(
                    entries = uiState.journalEntries,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun JournalEntriesList(
    entries: List<JournalEntry>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = entries.map { it.toUiModel() },
            key = { it.id }
        ) { journalEntry ->
            JournalEntryCard(
                entry = journalEntry,
                onClick = {}, // TODO: Navigate to journal details
                modifier = Modifier.padding(8.dp),
                onPlayClick = {} // TODO: Start playback
            )
        }
    }
}

@Composable
fun StartRecordingButton(
    onNavigateToRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onNavigateToRecording,
        modifier = modifier.padding(8.dp),
        content = {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Start recording a new journal entry",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Start Recording")
        }
    )
}
