package com.example.anothersmartvoicejournal.core.domain.repository

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun getAllEntries(): Flow<List<JournalEntry>>
    fun getPublishedEntries(): Flow<List<JournalEntry>>
    suspend fun getEntryById(id: String): JournalEntry?
    suspend fun saveEntry(entry: JournalEntry): Result<Unit>
    suspend fun deleteEntry(id: String): Result<Unit>
    fun searchEntries(query: String): Flow<List<JournalEntry>>
    fun getEntryCount(): Flow<Int>
}
