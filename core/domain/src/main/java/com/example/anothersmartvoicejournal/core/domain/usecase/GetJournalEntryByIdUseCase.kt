package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import javax.inject.Inject

/**
 * Use case to get a specific journal entry by its ID.
 * Returns the journal entry if found, or null if not found.
 */
class GetJournalEntryByIdUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    /**
     * Executes the use case to get a journal entry by ID.
     * @param entryId The ID of the journal entry to retrieve
     * @return The journal entry or null if not found
     */
    suspend operator fun invoke(entryId: String): JournalEntry? {
        return journalRepository.getEntryById(entryId)
    }
}
