package com.example.anothersmartvoicejournal.feature.journal.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.components.JournalEntryCard
import com.example.anothersmartvoicejournal.core.ui.components.TopAppBar
import com.example.anothersmartvoicejournal.core.ui.model.JournalEntryUiModel
import com.example.anothersmartvoicejournal.core.ui.model.TopAppBarUiModel

@Composable
fun JournalScreen(
    onNavigateToRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    val journalEntryList = listOf(
        JournalEntryUiModel(
            id = "id01",
            title = "Title 1",
            date = "09/11/1992",
            summary = "I've born.",
            hasAudio = false
        ),
        JournalEntryUiModel(id = "id02", title = "Title 2", date = "09/11/1993", summary = "I'm 1.", hasAudio = false),
        JournalEntryUiModel(id = "id03", title = "Title 3", date = "09/11/1994", summary = "I'm 2.", hasAudio = false),
        JournalEntryUiModel(id = "id04", title = "Title 4", date = "09/11/1995", summary = "I'm 3.", hasAudio = false),
        JournalEntryUiModel(id = "id05", title = "Title 5", date = "09/11/1996", summary = "I'm 4.", hasAudio = false),
        JournalEntryUiModel(id = "id06", title = "Title 6", date = "09/11/1997", summary = "I'm 5.", hasAudio = false),
        JournalEntryUiModel(id = "id07", title = "Title 7", date = "09/11/1998", summary = "I'm 6.", hasAudio = false),
        JournalEntryUiModel(id = "id08", title = "Title 8", date = "09/11/1999", summary = "I'm 7.", hasAudio = false),
        JournalEntryUiModel(id = "id09", title = "Title 9", date = "09/11/2000", summary = "I'm 8.", hasAudio = false),
        JournalEntryUiModel(id = "id10", title = "Title 10", date = "09/11/2001", summary = "I'm 9.", hasAudio = false),
        JournalEntryUiModel(
            id = "id11",
            title = "Title 11",
            date = "09/11/2002",
            summary = "I'm 10.",
            hasAudio = false
        ),
        JournalEntryUiModel(
            id = "id12",
            title = "Title 12",
            date = "09/11/2003",
            summary = "I'm 11.",
            hasAudio = false
        )
    )

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
        floatingActionButton = { FAB(onNavigateToRecording = onNavigateToRecording) },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        content = { innerPadding ->
            JournalScreenContent(journalEntryList = journalEntryList, modifier = Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun JournalScreenContent(
    journalEntryList: List<JournalEntryUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(items = journalEntryList, key = { it.id }) { journalEntry ->
                JournalEntryCard(
                    entry = journalEntry,
                    onClick = {},
                    modifier = Modifier.padding(8.dp),
                    onPlayClick = {}
                )
            }
        }
    }
}

@Composable
fun FAB(onNavigateToRecording: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = onNavigateToRecording,
        modifier = modifier.padding(8.dp),
        content = {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Start Recording")
        }
    )
}
