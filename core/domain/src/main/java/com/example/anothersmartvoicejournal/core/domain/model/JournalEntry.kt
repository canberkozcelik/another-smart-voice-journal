package com.example.anothersmartvoicejournal.core.domain.model

import java.util.Locale

data class JournalEntry(
    val id: String,
    val title: String,
    val content: String,
    val audioFilePath: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val duration: Long?,
    val language: String,
    val transcriptionConfidence: Float?,
    val isDraft: Boolean = false
) {
    val formattedDate: String
        get() = java.text.SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            .format(java.util.Date(createdAt))

    val formattedTime: String
        get() = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(java.util.Date(createdAt))

    val durationFormatted: String
        get() = duration?.let {
            val minutes = it / 60000
            val seconds = (it % 60000) / 1000
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        } ?: "00:00"
}
