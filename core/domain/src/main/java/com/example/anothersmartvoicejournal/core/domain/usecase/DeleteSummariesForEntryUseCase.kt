package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import javax.inject.Inject

/**
 * Use case to delete all summaries for a specific journal entry.
 * Useful when deleting a journal entry or regenerating summaries.
 */
class DeleteSummariesForEntryUseCase @Inject constructor(
    private val summaryRepository: SummaryRepository
) {
    /**
     * Executes the use case to delete all summaries for a journal entry.
     * @param entryId The ID of the journal entry whose summaries should be deleted
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(entryId: String): Result<Unit> {
        return summaryRepository.deleteSummariesForEntry(entryId)
    }
} 