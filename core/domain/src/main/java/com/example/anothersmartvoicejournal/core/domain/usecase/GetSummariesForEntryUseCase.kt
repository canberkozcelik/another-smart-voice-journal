package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.Summary
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get all summaries for a specific journal entry.
 * Returns summaries ordered by creation date (newest first).
 */
class GetSummariesForEntryUseCase @Inject constructor(
    private val summaryRepository: SummaryRepository
) {
    /**
     * Executes the use case to get summaries for a journal entry.
     * @param entryId The ID of the journal entry
     * @return Flow of summaries for the entry
     */
    operator fun invoke(entryId: String): Flow<List<Summary>> {
        return summaryRepository.getSummariesForEntry(entryId)
    }
} 