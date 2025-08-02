package com.example.anothersmartvoicejournal.core.common.mapper

import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JournalEntryUiMapperTest {

    @Test
    fun `toUiModel should map JournalEntry to JournalEntryUiModel correctly`() {
        // Given
        val journalEntry = JournalEntry(
            id = "test-id",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )

        // When
        val result = journalEntry.toUiModel()

        // Then
        assertEquals("test-id", result.id)
        assertEquals("Test Entry", result.title)
        assertEquals("Jan 15, 1970", result.date) // Expected date format
        assertEquals(null, result.summary) // Currently null as per implementation
        assertTrue(result.hasAudio)
    }

    @Test
    fun `toUiModel should set hasAudio to false when audioFilePath is null`() {
        // Given
        val journalEntry = JournalEntry(
            id = "test-id",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = null,
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )

        // When
        val result = journalEntry.toUiModel()

        // Then
        assertFalse(result.hasAudio)
    }

    @Test
    fun `toUiModel should set hasAudio to false when audioFilePath is empty`() {
        // Given
        val journalEntry = JournalEntry(
            id = "test-id",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )

        // When
        val result = journalEntry.toUiModel()

        // Then
        assertFalse(result.hasAudio)
    }
}
