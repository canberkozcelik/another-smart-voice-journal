package com.example.anothersmartvoicejournal.feature.journal.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import javax.inject.Inject

class GetJournalEntryByIdUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    suspend operator fun invoke(id: String): JournalEntry? {
        return journalRepository.getEntryById(id)
    }
}
