package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.Summary
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import javax.inject.Inject

/**
 * Use case to save a summary.
 * Handles both creating new summaries and updating existing ones.
 */
class SaveSummaryUseCase @Inject constructor(
    private val summaryRepository: SummaryRepository
) {
    /**
     * Executes the use case to save a summary.
     * @param summary The summary to save
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(summary: Summary): Result<Unit> {
        return summaryRepository.saveSummary(summary)
    }
} 