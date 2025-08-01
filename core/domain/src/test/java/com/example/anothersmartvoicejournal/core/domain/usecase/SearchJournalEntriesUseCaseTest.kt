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

class SearchJournalEntriesUseCaseTest {

    private lateinit var useCase: SearchJournalEntriesUseCase
    private lateinit var mockRepository: JournalRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = SearchJournalEntriesUseCase(mockRepository)
    }

    @Test
    fun `invoke should return matching entries for search query`() = runTest {
        // Given
        val query = "meeting"
        val expectedEntries = listOf(
            JournalEntry(
                id = "1",
                title = "Team Meeting Notes",
                content = "Discussed project timeline and next steps",
                audioFilePath = "/path/to/audio1",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                duration = 1000L,
                language = "en-US",
                transcriptionConfidence = 1.0f
            ),
            JournalEntry(
                id = "2",
                title = "Daily Reflection",
                content = "Had a productive meeting with the client",
                audioFilePath = "/path/to/audio2",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                duration = 1000L,
                language = "en-US",
                transcriptionConfidence = 1.0f
            )
        )
        coEvery { mockRepository.searchEntries(query) } returns flowOf(expectedEntries)

        // When
        val result = useCase(query)

        // Then
        assertEquals(expectedEntries, result.first())
    }

    @Test
    fun `invoke should return empty list for no matches`() = runTest {
        // Given
        val query = "nonexistent"
        val expectedEntries = emptyList<JournalEntry>()
        coEvery { mockRepository.searchEntries(query) } returns flowOf(expectedEntries)

        // When
        val result = useCase(query)

        // Then
        assertEquals(expectedEntries, result.first())
    }

    @Test
    fun `invoke should handle empty query`() = runTest {
        // Given
        val query = ""
        val expectedEntries = emptyList<JournalEntry>()
        coEvery { mockRepository.searchEntries(query) } returns flowOf(expectedEntries)

        // When
        val result = useCase(query)

        // Then
        assertEquals(expectedEntries, result.first())
    }
} 