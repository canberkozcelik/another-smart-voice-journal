package com.example.anothersmartvoicejournal.core.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.anothersmartvoicejournal.core.data.dao.JournalDao
import com.example.anothersmartvoicejournal.core.data.database.VoiceJournalDatabase
import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JournalRepositoryIntegrationTest {

    private lateinit var database: VoiceJournalDatabase
    private lateinit var journalDao: JournalDao
    private lateinit var journalRepository: JournalRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            VoiceJournalDatabase::class.java
        ).build()
        journalDao = database.journalDao()
        journalRepository = JournalRepositoryImpl(journalDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveEntry_should_persist_entry_to_database() = runTest {
        // Given
        val domainEntry = JournalEntry(
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
        val result = journalRepository.saveEntry(domainEntry)

        // Then
        assertTrue(result.isSuccess)

        val savedEntry = journalRepository.getEntryById("1")
        assertNotNull(savedEntry)
        assertEquals("1", savedEntry!!.id)
        assertEquals("Test Entry", savedEntry.title)
        assertEquals("Test content", savedEntry.content)
        assertEquals("/path/to/audio.mp3", savedEntry.audioFilePath)
        assertEquals(60000L, savedEntry.duration)
        assertEquals("en", savedEntry.language)
        assertEquals(0.95f, savedEntry.transcriptionConfidence)
        assertFalse(savedEntry.isDraft)
    }

    @Test
    fun getAllEntries_should_return_all_entries_from_database() = runTest {
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

        journalRepository.saveEntry(entry1)
        journalRepository.saveEntry(entry2)

        // When
        val result = journalRepository.getAllEntries().first()

        // Then
        assertEquals(2, result.size)
        // Order is by createdAt DESC, so newer entry (entry2) comes first
        assertEquals("2", result[0].id)
        assertEquals("Second Entry", result[0].title)
        assertTrue(result[0].isDraft)
        assertEquals("1", result[1].id)
        assertEquals("First Entry", result[1].title)
        assertFalse(result[1].isDraft)
    }

    @Test
    fun getPublishedEntries_should_return_only_non_draft_entries() = runTest {
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

        journalRepository.saveEntry(publishedEntry)
        journalRepository.saveEntry(draftEntry)

        // When
        val result = journalRepository.getPublishedEntries().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Published Entry", result[0].title)
        assertFalse(result[0].isDraft)
    }

    @Test
    fun searchEntries_should_return_matching_entries() = runTest {
        // Given
        val searchableEntry = JournalEntry(
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
        val regularEntry = JournalEntry(
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

        journalRepository.saveEntry(searchableEntry)
        journalRepository.saveEntry(regularEntry)

        // When
        val result = journalRepository.searchEntries("search").first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Searchable Entry", result[0].title)
    }

    @Test
    fun deleteEntry_should_remove_entry_from_database() = runTest {
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

        journalRepository.saveEntry(entry)

        // Verify entry exists
        assertNotNull(journalRepository.getEntryById("1"))

        // When
        val result = journalRepository.deleteEntry("1")

        // Then
        assertTrue(result.isSuccess)
        assertNull(journalRepository.getEntryById("1"))
    }

    @Test
    fun getEntryCount_should_return_correct_count() = runTest {
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

        journalRepository.saveEntry(entry1)
        journalRepository.saveEntry(entry2)

        // When
        val result = journalRepository.getEntryCount().first()

        // Then
        assertEquals(2, result)
    }

    @Test
    fun getEntryById_should_return_null_for_non_existent_entry() = runTest {
        // When
        val result = journalRepository.getEntryById("non-existent")

        // Then
        assertNull(result)
    }
}
