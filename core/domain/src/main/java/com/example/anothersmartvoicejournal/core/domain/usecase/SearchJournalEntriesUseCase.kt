package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case to search journal entries by query.
 * Searches through entry content and title.
 */
class SearchJournalEntriesUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    /**
     * Executes the use case to search journal entries.
     * @param query The search query
     * @return Flow of matching journal entries
     */
    operator fun invoke(query: String): Flow<List<JournalEntry>> {
        return journalRepository.searchEntries(query)
    }
}
