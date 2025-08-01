package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import javax.inject.Inject

/**
 * Use case to save a journal entry.
 * Handles both creating new entries and updating existing ones.
 */
class SaveJournalEntryUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    /**
     * Executes the use case to save a journal entry.
     * @param journalEntry The journal entry to save
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(journalEntry: JournalEntry): Result<Unit> {
        return journalRepository.saveEntry(journalEntry)
    }
} 