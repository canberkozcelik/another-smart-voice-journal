package com.example.anothersmartvoicejournal.feature.journal.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetJournalEntriesUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    operator fun invoke(): Flow<List<JournalEntry>> {
        return journalRepository.getAllEntries()
    }
}
