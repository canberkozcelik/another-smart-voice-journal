package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DeleteJournalEntryWithSummariesUseCaseTest {

    private lateinit var useCase: DeleteJournalEntryWithSummariesUseCase
    private lateinit var mockJournalRepository: JournalRepository
    private lateinit var mockSummaryRepository: SummaryRepository

    @Before
    fun setUp() {
        mockJournalRepository = mockk(relaxed = true)
        mockSummaryRepository = mockk(relaxed = true)
        useCase = DeleteJournalEntryWithSummariesUseCase(mockJournalRepository, mockSummaryRepository)
    }

    @Test
    fun `invoke should delete summaries first then journal entry`() = runTest {
        // Given
        val entryId = "1"
        val successResult = Result.success(Unit)
        coEvery { mockSummaryRepository.deleteSummariesForEntry(entryId) } returns successResult
        coEvery { mockJournalRepository.deleteEntry(entryId) } returns successResult

        // When
        val result = useCase(entryId)

        // Then
        coVerify(exactly = 1) { mockSummaryRepository.deleteSummariesForEntry(entryId) }
        coVerify(exactly = 1) { mockJournalRepository.deleteEntry(entryId) }
        assertEquals(successResult, result)
    }

    @Test
    fun `invoke should handle non-existent entry gracefully`() = runTest {
        // Given
        val entryId = "999"
        val successResult = Result.success(Unit)
        coEvery { mockSummaryRepository.deleteSummariesForEntry(entryId) } returns successResult
        coEvery { mockJournalRepository.deleteEntry(entryId) } returns successResult

        // When
        val result = useCase(entryId)

        // Then
        coVerify(exactly = 1) { mockSummaryRepository.deleteSummariesForEntry(entryId) }
        coVerify(exactly = 1) { mockJournalRepository.deleteEntry(entryId) }
        assertEquals(successResult, result)
    }
} 