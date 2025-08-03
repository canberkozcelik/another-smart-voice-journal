package com.example.anothersmartvoicejournal.core.ui.model

data class JournalEntryUiModel(
    val id: String,
    val title: String,
    val date: String,
    val summary: String?,
    val hasAudio: Boolean
)
