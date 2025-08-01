package com.example.anothersmartvoicejournal.core.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.anothersmartvoicejournal.core.data.dao.JournalDao
import com.example.anothersmartvoicejournal.core.data.dao.SummaryDao
import com.example.anothersmartvoicejournal.core.data.database.VoiceJournalDatabase
import com.example.anothersmartvoicejournal.core.data.entity.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.model.Summary
import java.io.IOException
import kotlin.test.assertEquals
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
class SummaryRepositoryIntegrationTest {

    private lateinit var database: VoiceJournalDatabase
    private lateinit var summaryDao: SummaryDao
    private lateinit var journalDao: JournalDao
    private lateinit var summaryRepository: SummaryRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            VoiceJournalDatabase::class.java
        ).build()
        summaryDao = database.summaryDao()
        journalDao = database.journalDao()
        summaryRepository = SummaryRepositoryImpl(summaryDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveSummary_should_persist_summary_to_database() = runTest {
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
        val domainSummary = Summary(
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
        val result = summaryRepository.saveSummary(domainSummary)

        // Then
        assertTrue(result.isSuccess)

        val savedSummary = summaryRepository.getSummaryById("1")
        assertNotNull(savedSummary)
        assertEquals("1", savedSummary!!.id)
        assertEquals("entry1", savedSummary.entryId)
        assertEquals("• First bullet point\n• Second bullet point", savedSummary.content)
        assertEquals(2, savedSummary.bulletPoints)
        assertEquals("ARTICLE", savedSummary.inputType)
        assertEquals(0.95f, savedSummary.confidence)
        assertEquals(2, savedSummary.bulletPointList.size)
        assertEquals("• First bullet point", savedSummary.bulletPointList[0])
        assertEquals("• Second bullet point", savedSummary.bulletPointList[1])
    }

    @Test
    fun getSummariesForEntry_should_return_all_summaries_for_entry() = runTest {
        // Given
        val journalEntry1 = JournalEntry(
            id = "entry1",
            title = "Test Entry 1",
            content = "Test content 1",
            audioFilePath = "/path/to/audio1.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val journalEntry2 = JournalEntry(
            id = "entry2",
            title = "Test Entry 2",
            content = "Test content 2",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
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
        val summary3 = Summary(
            id = "3",
            entryId = "entry2",
            content = "• Different entry summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567892L,
            confidence = 0.90f
        )

        journalDao.insertEntry(journalEntry1)
        journalDao.insertEntry(journalEntry2)
        summaryRepository.saveSummary(summary1)
        summaryRepository.saveSummary(summary2)
        summaryRepository.saveSummary(summary3)

        // When
        val result = summaryRepository.getSummariesForEntry("entry1").first()

        // Then
        assertEquals(2, result.size)
        // Order is by createdAt DESC, so newer summary (summary2) comes first
        assertEquals("2", result[0].id)
        assertEquals("entry1", result[0].entryId)
        assertEquals("CONVERSATION", result[0].inputType)
        assertEquals("1", result[1].id)
        assertEquals("entry1", result[1].entryId)
        assertEquals("ARTICLE", result[1].inputType)
    }

    @Test
    fun getSummaryById_should_return_specific_summary() = runTest {
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
            content = "• Test summary with multiple points\n• Second point\n• Third point",
            bulletPoints = 3,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        journalDao.insertEntry(journalEntry)
        summaryRepository.saveSummary(summary)

        // When
        val result = summaryRepository.getSummaryById("1")

        // Then
        assertNotNull(result)
        assertEquals("1", result!!.id)
        assertEquals("entry1", result.entryId)
        assertEquals("• Test summary with multiple points\n• Second point\n• Third point", result.content)
        assertEquals(3, result.bulletPoints)
        assertEquals("ARTICLE", result.inputType)
        assertEquals(0.95f, result.confidence)
        assertEquals(3, result.bulletPointList.size)
        assertEquals("• Test summary with multiple points", result.bulletPointList[0])
        assertEquals("• Second point", result.bulletPointList[1])
        assertEquals("• Third point", result.bulletPointList[2])
    }

    @Test
    fun getSummaryById_should_return_null_for_non_existent_summary() = runTest {
        // When
        val result = summaryRepository.getSummaryById("non-existent")

        // Then
        assertNull(result)
    }

    @Test
    fun deleteSummary_should_remove_summary_from_database() = runTest {
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
        summaryRepository.saveSummary(summary)

        // Verify summary exists
        assertNotNull(summaryRepository.getSummaryById("1"))

        // When
        val result = summaryRepository.deleteSummary("1")

        // Then
        assertTrue(result.isSuccess)
        assertNull(summaryRepository.getSummaryById("1"))
    }

    @Test
    fun deleteSummariesForEntry_should_remove_all_summaries_for_entry() = runTest {
        // Given
        val journalEntry1 = JournalEntry(
            id = "entry1",
            title = "Test Entry 1",
            content = "Test content 1",
            audioFilePath = "/path/to/audio1.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val journalEntry2 = JournalEntry(
            id = "entry2",
            title = "Test Entry 2",
            content = "Test content 2",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
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
        val summary3 = Summary(
            id = "3",
            entryId = "entry2",
            content = "• Different entry summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567892L,
            confidence = 0.90f
        )

        journalDao.insertEntry(journalEntry1)
        journalDao.insertEntry(journalEntry2)
        summaryRepository.saveSummary(summary1)
        summaryRepository.saveSummary(summary2)
        summaryRepository.saveSummary(summary3)

        // Verify summaries exist
        assertEquals(2, summaryRepository.getSummariesForEntry("entry1").first().size)
        assertEquals(1, summaryRepository.getSummariesForEntry("entry2").first().size)

        // When
        val result = summaryRepository.deleteSummariesForEntry("entry1")

        // Then
        assertTrue(result.isSuccess)
        assertEquals(0, summaryRepository.getSummariesForEntry("entry1").first().size)
        assertEquals(1, summaryRepository.getSummariesForEntry("entry2").first().size)
    }

    @Test
    fun getRecentSummaries_should_return_limited_results() = runTest {
        // Given
        val journalEntry1 = JournalEntry(
            id = "entry1",
            title = "Test Entry 1",
            content = "Test content 1",
            audioFilePath = "/path/to/audio1.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val journalEntry2 = JournalEntry(
            id = "entry2",
            title = "Test Entry 2",
            content = "Test content 2",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )
        val journalEntry3 = JournalEntry(
            id = "entry3",
            title = "Test Entry 3",
            content = "Test content 3",
            audioFilePath = "/path/to/audio3.mp3",
            createdAt = 1234567892L,
            updatedAt = 1234567892L,
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
            entryId = "entry2",
            content = "• Second summary",
            bulletPoints = 1,
            inputType = "CONVERSATION",
            createdAt = 1234567891L,
            confidence = 0.88f
        )
        val summary3 = Summary(
            id = "3",
            entryId = "entry3",
            content = "• Third summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567892L,
            confidence = 0.90f
        )

        journalDao.insertEntry(journalEntry1)
        journalDao.insertEntry(journalEntry2)
        journalDao.insertEntry(journalEntry3)
        summaryRepository.saveSummary(summary1)
        summaryRepository.saveSummary(summary2)
        summaryRepository.saveSummary(summary3)

        // When
        val result = summaryRepository.getRecentSummaries(2).first()

        // Then
        assertEquals(2, result.size)
        // Should return most recent summaries (3 and 2)
        assertEquals("3", result[0].id)
        assertEquals("2", result[1].id)
    }

    @Test
    fun bulletPointList_should_parse_content_correctly() = runTest {
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
            content = "• Point one\n• Point two\n• Point three\n• Point four",
            bulletPoints = 4,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        journalDao.insertEntry(journalEntry)
        summaryRepository.saveSummary(summary)

        // When
        val result = summaryRepository.getSummaryById("1")

        // Then
        assertNotNull(result)
        assertEquals(4, result!!.bulletPointList.size)
        assertEquals("• Point one", result.bulletPointList[0])
        assertEquals("• Point two", result.bulletPointList[1])
        assertEquals("• Point three", result.bulletPointList[2])
        assertEquals("• Point four", result.bulletPointList[3])
    }

    @Test
    fun bulletPointList_should_handle_empty_content() = runTest {
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
            content = "",
            bulletPoints = 0,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        journalDao.insertEntry(journalEntry)
        summaryRepository.saveSummary(summary)

        // When
        val result = summaryRepository.getSummaryById("1")

        // Then
        assertNotNull(result)
        assertEquals(0, result!!.bulletPointList.size)
    }
}
