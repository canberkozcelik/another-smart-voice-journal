package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetJournalEntriesUseCaseTest {

    private lateinit var useCase: GetJournalEntriesUseCase
    private lateinit var mockRepository: JournalRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetJournalEntriesUseCase(mockRepository)
    }

    @Test
    fun `invoke should return flow from repository`() = runTest {
        // Given
        val expectedEntries = listOf(
            JournalEntry(
                id = "1",
                title = "Test Entry 1",
                content = "Test content 1",
                audioFilePath = "/path/to/audio1",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                duration = 1000L,
                language = "en-US",
                transcriptionConfidence = 1.0f
            ),
            JournalEntry(
                id = "2",
                title = "Test Entry 2",
                content = "Test content 2",
                audioFilePath = "/path/to/audio2",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                duration = 1000L,
                language = "en-US",
                transcriptionConfidence = 1.0f
            )
        )
        coEvery { mockRepository.getAllEntries() } returns flowOf(expectedEntries)

        // When
        val result = useCase()

        // Then
        assertEquals(expectedEntries, result.first())
    }
}
