package com.example.anothersmartvoicejournal.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val audioFilePath: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val duration: Long?,
    val language: String,
    val transcriptionConfidence: Float?,
    val isDraft: Boolean = false
)
