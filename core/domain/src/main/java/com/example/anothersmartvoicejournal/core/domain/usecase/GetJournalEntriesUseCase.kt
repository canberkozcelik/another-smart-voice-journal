package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get all journal entries.
 * Returns a Flow of all journal entries ordered by creation date (newest first).
 */
class GetJournalEntriesUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    /**
     * Executes the use case to get all journal entries.
     * @return Flow of all journal entries
     */
    operator fun invoke(): Flow<List<JournalEntry>> {
        return journalRepository.getAllEntries()
    }
} 