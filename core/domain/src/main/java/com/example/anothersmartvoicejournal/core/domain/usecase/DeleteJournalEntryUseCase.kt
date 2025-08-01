package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import javax.inject.Inject

/**
 * Use case to delete a journal entry by its ID.
 * Also deletes associated summaries when the entry is deleted.
 */
class DeleteJournalEntryUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    /**
     * Executes the use case to delete a journal entry.
     * @param entryId The ID of the journal entry to delete
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(entryId: String): Result<Unit> {
        return journalRepository.deleteEntry(entryId)
    }
} 