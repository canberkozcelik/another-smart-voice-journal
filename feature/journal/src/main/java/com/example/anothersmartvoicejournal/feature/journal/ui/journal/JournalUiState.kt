package com.example.anothersmartvoicejournal.feature.journal.ui.journal

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry

data class JournalUiState(
    val isLoading: Boolean = false,
    val journalEntries: List<JournalEntry> = emptyList(),
    val error: String? = null
)
