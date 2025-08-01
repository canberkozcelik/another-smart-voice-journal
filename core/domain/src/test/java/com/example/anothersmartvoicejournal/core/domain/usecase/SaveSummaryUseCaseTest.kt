package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.Summary
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveSummaryUseCaseTest {

    private lateinit var useCase: SaveSummaryUseCase
    private lateinit var mockRepository: SummaryRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = SaveSummaryUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success result for new summary`() = runTest {
        // Given
        val summary = Summary(
            id = "0", // New summary
            entryId = "1",
            content = "Test summary content",
            createdAt = System.currentTimeMillis(),
            bulletPoints = 3,
            inputType = "JOURNAL",
            confidence = 1.0f
        )
        val expectedResult = Result.success(Unit)
        coEvery { mockRepository.saveSummary(summary) } returns expectedResult

        // When
        val result = useCase(summary)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should handle existing summary update`() = runTest {
        // Given
        val existingSummary = Summary(
            id = "1", // Existing summary
            entryId = "1",
            content = "Updated summary content",
            createdAt = System.currentTimeMillis(),
            bulletPoints = 3,
            inputType = "JOURNAL",
            confidence = 1.0f
        )
        val expectedResult = Result.success(Unit)
        coEvery { mockRepository.saveSummary(existingSummary) } returns expectedResult

        // When
        val result = useCase(existingSummary)

        // Then
        assertEquals(expectedResult, result)
    }
} 