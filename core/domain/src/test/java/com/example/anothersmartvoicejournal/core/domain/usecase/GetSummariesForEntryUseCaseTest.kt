package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.Summary
import com.example.anothersmartvoicejournal.core.domain.repository.SummaryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSummariesForEntryUseCaseTest {

    private lateinit var useCase: GetSummariesForEntryUseCase
    private lateinit var mockRepository: SummaryRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetSummariesForEntryUseCase(mockRepository)
    }

    @Test
    fun `invoke should return summaries for entry`() = runTest {
        // Given
        val entryId = "1"
        val expectedSummaries = listOf(
            Summary(
                id = "1",
                entryId = entryId,
                content = "Summary 1",
                createdAt = System.currentTimeMillis(),
                bulletPoints = 3,
                inputType = "JOURNAL",
                confidence = 1.0f
            ),
            Summary(
                id = "2",
                entryId = entryId,
                content = "Summary 2",
                createdAt = System.currentTimeMillis(),
                bulletPoints = 3,
                inputType = "JOURNAL",
                confidence = 1.0f
            )
        )
        coEvery { mockRepository.getSummariesForEntry(entryId) } returns flowOf(expectedSummaries)

        // When
        val result = useCase(entryId)

        // Then
        assertEquals(expectedSummaries, result.first())
    }

    @Test
    fun `invoke should return empty list for entry with no summaries`() = runTest {
        // Given
        val entryId = "999"
        val expectedSummaries = emptyList<Summary>()
        coEvery { mockRepository.getSummariesForEntry(entryId) } returns flowOf(expectedSummaries)

        // When
        val result = useCase(entryId)

        // Then
        assertEquals(expectedSummaries, result.first())
    }
}
