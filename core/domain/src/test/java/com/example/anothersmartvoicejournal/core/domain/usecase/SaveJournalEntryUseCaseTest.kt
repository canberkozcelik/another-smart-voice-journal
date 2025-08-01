package com.example.anothersmartvoicejournal.core.domain.usecase

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.repository.JournalRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveJournalEntryUseCaseTest {

    private lateinit var useCase: SaveJournalEntryUseCase
    private lateinit var mockRepository: JournalRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = SaveJournalEntryUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success result for new entry`() = runTest {
        // Given
        val journalEntry = JournalEntry(
            id = "0", // New entry
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            duration = 1000L,
            language = "en-US",
            transcriptionConfidence = 1.0f
        )
        val expectedResult = Result.success(Unit)
        coEvery { mockRepository.saveEntry(journalEntry) } returns expectedResult

        // When
        val result = useCase(journalEntry)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should handle existing entry update`() = runTest {
        // Given
        val existingEntry = JournalEntry(
            id = "1", // Existing entry
            title = "Updated Entry",
            content = "Updated content",
            audioFilePath = "/path/to/audio",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            duration = 1000L,
            language = "en-US",
            transcriptionConfidence = 1.0f
        )
        val expectedResult = Result.success(Unit)
        coEvery { mockRepository.saveEntry(existingEntry) } returns expectedResult

        // When
        val result = useCase(existingEntry)

        // Then
        assertEquals(expectedResult, result)
    }
} 