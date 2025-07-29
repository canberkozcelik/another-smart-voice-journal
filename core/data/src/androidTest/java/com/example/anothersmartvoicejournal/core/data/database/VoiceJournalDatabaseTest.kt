package com.example.anothersmartvoicejournal.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.anothersmartvoicejournal.core.data.dao.JournalDao
import com.example.anothersmartvoicejournal.core.data.dao.SummaryDao
import com.example.anothersmartvoicejournal.core.data.entity.JournalEntry
import com.example.anothersmartvoicejournal.core.data.entity.Summary
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(AndroidJUnit4::class)
class VoiceJournalDatabaseTest {
    
    private lateinit var database: VoiceJournalDatabase
    private lateinit var journalDao: JournalDao
    private lateinit var summaryDao: SummaryDao
    
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, VoiceJournalDatabase::class.java
        ).build()
        journalDao = database.journalDao()
        summaryDao = database.summaryDao()
    }
    
    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }
    
    @Test
    fun insertAndReadJournalEntry() = runTest {
        // Given
        val entry = JournalEntry(
            id = "1",
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
        journalDao.insertEntry(entry)
        val result = journalDao.getEntryById("1")
        
        // Then
        assertNotNull(result)
        assertEquals("1", result.id)
        assertEquals("Test Entry", result.title)
        assertEquals("Test content", result.content)
        assertEquals("/path/to/audio.mp3", result.audioFilePath)
        assertEquals(1234567890L, result.createdAt)
        assertEquals(1234567890L, result.updatedAt)
        assertEquals(60000L, result.duration)
        assertEquals("en", result.language)
        assertEquals(0.95f, result.transcriptionConfidence)
        assertEquals(false, result.isDraft)
    }
    
    @Test
    fun insertAndReadSummary() = runTest {
        // Given
        val journalEntry = JournalEntry(
            id = "entry1",
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
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "• First bullet point\n• Second bullet point",
            bulletPoints = 2,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        
        // When
        journalDao.insertEntry(journalEntry)
        summaryDao.insertSummary(summary)
        val result = summaryDao.getSummaryById("1")
        
        // Then
        assertNotNull(result)
        assertEquals("1", result.id)
        assertEquals("entry1", result.entryId)
        assertEquals("• First bullet point\n• Second bullet point", result.content)
        assertEquals(2, result.bulletPoints)
        assertEquals("ARTICLE", result.inputType)
        assertEquals(0.95f, result.confidence)
    }
    
    @Test
    fun getAllEntries() = runTest {
        // Given
        val entry1 = JournalEntry(
            id = "1",
            title = "First Entry",
            content = "First content",
            audioFilePath = "/path/to/audio1.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val entry2 = JournalEntry(
            id = "2",
            title = "Second Entry",
            content = "Second content",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
            duration = 120000L,
            language = "en",
            transcriptionConfidence = 0.88f,
            isDraft = true
        )
        
        journalDao.insertEntry(entry1)
        journalDao.insertEntry(entry2)
        
        // When
        val result = journalDao.getAllEntries().first()
        
        // Then
        assertEquals(2, result.size)
        // Order is by createdAt DESC, so newer entry (entry2) comes first
        assertEquals("2", result[0].id)
        assertEquals("Second Entry", result[0].title)
        assertEquals("1", result[1].id)
        assertEquals("First Entry", result[1].title)
    }
    
    @Test
    fun getPublishedEntries() = runTest {
        // Given
        val publishedEntry = JournalEntry(
            id = "1",
            title = "Published Entry",
            content = "Published content",
            audioFilePath = "/path/to/audio1.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val draftEntry = JournalEntry(
            id = "2",
            title = "Draft Entry",
            content = "Draft content",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
            duration = 120000L,
            language = "en",
            transcriptionConfidence = 0.88f,
            isDraft = true
        )
        
        journalDao.insertEntry(publishedEntry)
        journalDao.insertEntry(draftEntry)
        
        // When
        val result = journalDao.getPublishedEntries().first()
        
        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Published Entry", result[0].title)
        assertEquals(false, result[0].isDraft)
    }
    
    @Test
    fun searchEntries() = runTest {
        // Given
        val entry1 = JournalEntry(
            id = "1",
            title = "Searchable Entry",
            content = "This contains the search term",
            audioFilePath = "/path/to/audio1.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val entry2 = JournalEntry(
            id = "2",
            title = "Regular Entry",
            content = "This does not contain the term",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
            duration = 120000L,
            language = "en",
            transcriptionConfidence = 0.88f,
            isDraft = false
        )
        
        journalDao.insertEntry(entry1)
        journalDao.insertEntry(entry2)
        
        // When
        val result = journalDao.searchEntries("search").first()
        
        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Searchable Entry", result[0].title)
    }
    
    @Test
    fun getSummariesForEntry() = runTest {
        // Given
        val journalEntry = JournalEntry(
            id = "entry1",
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
        val summary1 = Summary(
            id = "1",
            entryId = "entry1",
            content = "• First summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        val summary2 = Summary(
            id = "2",
            entryId = "entry1",
            content = "• Second summary",
            bulletPoints = 1,
            inputType = "CONVERSATION",
            createdAt = 1234567891L,
            confidence = 0.88f
        )
        
        journalDao.insertEntry(journalEntry)
        summaryDao.insertSummary(summary1)
        summaryDao.insertSummary(summary2)
        
        // When
        val result = summaryDao.getSummariesForEntry("entry1").first()
        
        // Then
        assertEquals(2, result.size)
        // Order is by createdAt DESC, so newer summary (summary2) comes first
        assertEquals("2", result[0].id)
        assertEquals("entry1", result[0].entryId)
        assertEquals("1", result[1].id)
        assertEquals("entry1", result[1].entryId)
    }
    
    @Test
    fun deleteEntry() = runTest {
        // Given
        val entry = JournalEntry(
            id = "1",
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
        
        journalDao.insertEntry(entry)
        
        // Verify entry exists
        assertNotNull(journalDao.getEntryById("1"))
        
        // When
        journalDao.deleteEntryById("1")
        
        // Then
        assertNull(journalDao.getEntryById("1"))
    }
    
    @Test
    fun deleteSummary() = runTest {
        // Given
        val journalEntry = JournalEntry(
            id = "entry1",
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
        val summary = Summary(
            id = "1",
            entryId = "entry1",
            content = "• Test summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        
        journalDao.insertEntry(journalEntry)
        summaryDao.insertSummary(summary)
        
        // Verify summary exists
        assertNotNull(summaryDao.getSummaryById("1"))
        
        // When
        summaryDao.deleteSummary(summary)
        
        // Then
        assertNull(summaryDao.getSummaryById("1"))
    }
    
    @Test
    fun getEntryCount() = runTest {
        // Given
        val entry1 = JournalEntry(
            id = "1",
            title = "First Entry",
            content = "First content",
            audioFilePath = "/path/to/audio1.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val entry2 = JournalEntry(
            id = "2",
            title = "Second Entry",
            content = "Second content",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
            duration = 120000L,
            language = "en",
            transcriptionConfidence = 0.88f,
            isDraft = false
        )
        
        journalDao.insertEntry(entry1)
        journalDao.insertEntry(entry2)
        
        // When
        val result = journalDao.getEntryCount().first()
        
        // Then
        assertEquals(2, result)
    }
} 