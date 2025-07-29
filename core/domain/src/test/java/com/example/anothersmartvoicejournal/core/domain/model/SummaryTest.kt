package com.example.anothersmartvoicejournal.core.domain.model

import org.junit.Test
import kotlin.test.assertEquals

class SummaryTest {
    
    @Test
    fun `formattedDate should return correct date format`() {
        // Given
        val timestamp = 1234567890000L // Feb 14, 2009
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "• Test summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = timestamp,
            confidence = 0.95f
        )
        
        // When
        val formattedDate = summary.formattedDate
        
        // Then
        assertEquals("Feb 14, 2009", formattedDate)
    }
    
    @Test
    fun `bulletPointList should parse content correctly`() {
        // Given
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "• First bullet point\n• Second bullet point\n• Third bullet point",
            bulletPoints = 3,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        
        // When
        val bulletPoints = summary.bulletPointList
        
        // Then
        assertEquals(3, bulletPoints.size)
        assertEquals("• First bullet point", bulletPoints[0])
        assertEquals("• Second bullet point", bulletPoints[1])
        assertEquals("• Third bullet point", bulletPoints[2])
    }
    
    @Test
    fun `bulletPointList should handle empty content`() {
        // Given
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "",
            bulletPoints = 0,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        
        // When
        val bulletPoints = summary.bulletPointList
        
        // Then
        assertEquals(0, bulletPoints.size)
    }
    
    @Test
    fun `bulletPointList should handle content with empty lines`() {
        // Given
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "• First point\n\n• Second point\n  \n• Third point",
            bulletPoints = 3,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        
        // When
        val bulletPoints = summary.bulletPointList
        
        // Then
        assertEquals(3, bulletPoints.size)
        assertEquals("• First point", bulletPoints[0])
        assertEquals("• Second point", bulletPoints[1])
        assertEquals("• Third point", bulletPoints[2])
    }
    
    @Test
    fun `bulletPointList should handle single bullet point`() {
        // Given
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "• Single bullet point",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        
        // When
        val bulletPoints = summary.bulletPointList
        
        // Then
        assertEquals(1, bulletPoints.size)
        assertEquals("• Single bullet point", bulletPoints[0])
    }
    
    @Test
    fun `bulletPointList should trim whitespace`() {
        // Given
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "  • Point with spaces  \n  • Another point  ",
            bulletPoints = 2,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        
        // When
        val bulletPoints = summary.bulletPointList
        
        // Then
        assertEquals(2, bulletPoints.size)
        assertEquals("• Point with spaces", bulletPoints[0])
        assertEquals("• Another point", bulletPoints[1])
    }
} 