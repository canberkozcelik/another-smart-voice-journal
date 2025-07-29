package com.example.anothersmartvoicejournal.core.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "summaries",
    foreignKeys = [
        ForeignKey(
            entity = JournalEntry::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["entryId"])
    ]
)
data class Summary(
    @PrimaryKey val id: String,
    val entryId: String,
    val content: String,
    val bulletPoints: Int,
    val inputType: String, // "ARTICLE" or "CONVERSATION"
    val createdAt: Long,
    val confidence: Float?
) 