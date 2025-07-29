package com.example.anothersmartvoicejournal.core.domain.model

import junit.framework.TestCase.assertEquals
import org.junit.Test

class JournalEntryTest {
    
    @Test
    fun `formattedDate should return correct date format`() {
        // Given
        val timestamp = 1234567890000L // Feb 14, 2009
        val entry = JournalEntry(
            id = "1",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = timestamp,
            updatedAt = timestamp,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        
        // When
        val formattedDate = entry.formattedDate
        
        // Then
        assertEquals("Feb 14, 2009", formattedDate)
    }
    
    @Test
    fun `formattedTime should return correct time format`() {
        // Given
        val timestamp = 1234567890000L // Dec 13, 2008 15:31:30 UTC
        val entry = JournalEntry(
            id = "1",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = timestamp,
            updatedAt = timestamp,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        
        // When
        val formattedTime = entry.formattedTime
        
        // Then
        // Note: The exact time depends on the system's timezone
        // We just verify it's in HH:mm format
        assertEquals(5, formattedTime.length)
        assertEquals(':', formattedTime[2])
    }
    
    @Test
    fun `durationFormatted should return correct format for valid duration`() {
        // Given
        val entry = JournalEntry(
            id = "1",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 125000L, // 2 minutes 5 seconds
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        
        // When
        val formattedDuration = entry.durationFormatted
        
        // Then
        assertEquals("02:05", formattedDuration)
    }
    
    @Test
    fun `durationFormatted should return zero minutes zero seconds for null duration`() {
        // Given
        val entry = JournalEntry(
            id = "1",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = null,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        
        // When
        val formattedDuration = entry.durationFormatted
        
        // Then
        assertEquals("00:00", formattedDuration)
    }
    
    @Test
    fun `durationFormatted should handle zero duration`() {
        // Given
        val entry = JournalEntry(
            id = "1",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 0L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        
        // When
        val formattedDuration = entry.durationFormatted
        
        // Then
        assertEquals("00:00", formattedDuration)
    }
    
    @Test
    fun `durationFormatted should handle long duration`() {
        // Given
        val entry = JournalEntry(
            id = "1",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 3661000L, // 1 hour 1 minute 1 second
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        
        // When
        val formattedDuration = entry.durationFormatted
        
        // Then
        assertEquals("61:01", formattedDuration)
    }
} 