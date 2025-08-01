package com.example.anothersmartvoicejournal.core.domain.repository

import com.example.anothersmartvoicejournal.core.domain.model.Summary
import kotlinx.coroutines.flow.Flow

interface SummaryRepository {
    fun getSummariesForEntry(entryId: String): Flow<List<Summary>>
    suspend fun getSummaryById(id: String): Summary?
    suspend fun saveSummary(summary: Summary): Result<Unit>
    suspend fun deleteSummary(id: String): Result<Unit>
    suspend fun deleteSummariesForEntry(entryId: String): Result<Unit>
    fun getRecentSummaries(limit: Int = 10): Flow<List<Summary>>
}
