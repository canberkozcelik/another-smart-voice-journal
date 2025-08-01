package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import javax.inject.Inject

/**
 * Use case to delete a journal entry and all its associated summaries.
 * This ensures data consistency by deleting related summaries when an entry is deleted.
 */
class DeleteJournalEntryWithSummariesUseCase @Inject constructor(
    private val journalRepository: JournalRepository,
    private val summaryRepository: SummaryRepository
) {
    /**
     * Executes the use case to delete a journal entry and all its summaries.
     * @param entryId The ID of the journal entry to delete
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(entryId: String): Result<Unit> {
        // First delete all summaries for this entry
        val summaryResult = summaryRepository.deleteSummariesForEntry(entryId)
        if (summaryResult.isFailure) {
            return summaryResult
        }
        // Then delete the journal entry
        return journalRepository.deleteEntry(entryId)
    }
} 