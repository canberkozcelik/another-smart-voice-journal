package com.example.anothersmartvoicejournal.core.common.mapper

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.ui.model.JournalEntryUiModel
import java.text.SimpleDateFormat
import java.util.*

fun JournalEntry.toUiModel(): JournalEntryUiModel {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return JournalEntryUiModel(
        id = id,
        title = title,
        date = dateFormat.format(Date(createdAt)),
        summary = null, // To be filled if summary is available
        hasAudio = !audioFilePath.isNullOrEmpty()
    )
}
