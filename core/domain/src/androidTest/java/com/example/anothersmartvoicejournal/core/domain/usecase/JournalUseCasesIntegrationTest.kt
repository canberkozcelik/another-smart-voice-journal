package com.example.anothersmartvoicejournal.core.domain.usecase

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.anothersmartvoicejournal.core.data.database.VoiceJournalDatabase
import com.example.anothersmartvoicejournal.core.data.repository.JournalRepositoryImpl
import com.example.anothersmartvoicejournal.core.data.repository.SummaryRepositoryImpl
import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.model.Summary
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class JournalUseCasesIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var database: VoiceJournalDatabase
    private lateinit var journalRepository: JournalRepositoryImpl
    private lateinit var summaryRepository: SummaryRepositoryImpl

    // Use cases
    private lateinit var getJournalEntriesUseCase: GetJournalEntriesUseCase
    private lateinit var saveJournalEntryUseCase: SaveJournalEntryUseCase
    private lateinit var deleteJournalEntryWithSummariesUseCase: DeleteJournalEntryWithSummariesUseCase
    private lateinit var searchJournalEntriesUseCase: SearchJournalEntriesUseCase
    private lateinit var getSummariesForEntryUseCase: GetSummariesForEntryUseCase
    private lateinit var saveSummaryUseCase: SaveSummaryUseCase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VoiceJournalDatabase::class.java
        ).build()

        journalRepository = JournalRepositoryImpl(database.journalDao())
        summaryRepository = SummaryRepositoryImpl(database.summaryDao())

        // Initialize use cases
        getJournalEntriesUseCase = GetJournalEntriesUseCase(journalRepository)
        saveJournalEntryUseCase = SaveJournalEntryUseCase(journalRepository)
        deleteJournalEntryWithSummariesUseCase = DeleteJournalEntryWithSummariesUseCase(
            journalRepository, summaryRepository
        )
        searchJournalEntriesUseCase = SearchJournalEntriesUseCase(journalRepository)
        getSummariesForEntryUseCase = GetSummariesForEntryUseCase(summaryRepository)
        saveSummaryUseCase = SaveSummaryUseCase(summaryRepository)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveJournalEntry_should_persist_entry_and_return_success() = runTest {
        // Given
        val journalEntry = JournalEntry(
            id = "0",
            title = "Test Entry",
            content = "Test content for journal entry",
            audioFilePath = "/path/to/audio",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        // When
        val result = saveJournalEntryUseCase(journalEntry)

        // Then
        assertTrue(result.isSuccess)

        val entries = getJournalEntriesUseCase().first()
        assertEquals(1, entries.size)
        assertEquals("Test Entry", entries[0].title)
        assertEquals("Test content for journal entry", entries[0].content)
    }

    @Test
    fun getJournalEntries_should_return_all_entries_ordered_by_date() = runTest {
        // Given
        val entry1 = JournalEntry(
            id = "0",
            title = "First Entry",
            content = "First content",
            audioFilePath = "/path/to/audio1",
            createdAt = System.currentTimeMillis() - 1000,
            updatedAt = System.currentTimeMillis() - 1000
        )
        val entry2 = JournalEntry(
            id = "0",
            title = "Second Entry",
            content = "Second content",
            audioFilePath = "/path/to/audio2",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        saveJournalEntryUseCase(entry1)
        saveJournalEntryUseCase(entry2)

        // When
        val entries = getJournalEntriesUseCase().first()

        // Then
        assertEquals(2, entries.size)
        // Should be ordered by createdAt DESC (newest first)
        assertEquals("Second Entry", entries[0].title)
        assertEquals("First Entry", entries[1].title)
    }

    @Test
    fun searchJournalEntries_should_return_matching_entries() = runTest {
        // Given
        val entry1 = JournalEntry(
            id = "0",
            title = "Meeting Notes",
            content = "Discussed project timeline",
            audioFilePath = "/path/to/audio1",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val entry2 = JournalEntry(
            id = "0",
            title = "Daily Reflection",
            content = "Had a productive day",
            audioFilePath = "/path/to/audio2",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        saveJournalEntryUseCase(entry1)
        saveJournalEntryUseCase(entry2)

        // When
        val searchResults = searchJournalEntriesUseCase("meeting").first()

        // Then
        assertEquals(1, searchResults.size)
        assertEquals("Meeting Notes", searchResults[0].title)
    }

    @Test
    fun saveSummary_should_persist_summary_and_return_success() = runTest {
        // Given
        val journalEntry = JournalEntry(
            id = "0",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val saveResult = saveJournalEntryUseCase(journalEntry)
        assertTrue(saveResult.isSuccess)

        val summary = Summary(
            id = "0",
            entryId = "1", // Assuming the saved entry gets ID "1"
            content = "This is a test summary of the journal entry",
            bulletPoints = 3,
            inputType = "ARTICLE",
            createdAt = System.currentTimeMillis(),
            confidence = 0.95f
        )

        // When
        val result = saveSummaryUseCase(summary)

        // Then
        assertTrue(result.isSuccess)

        val summaries = getSummariesForEntryUseCase("1").first()
        assertEquals(1, summaries.size)
        assertEquals("This is a test summary of the journal entry", summaries[0].content)
    }

    @Test
    fun deleteJournalEntryWithSummaries_should_delete_entry_and_summaries() = runTest {
        // Given
        val journalEntry = JournalEntry(
            id = "0",
            title = "Test Entry",
            content = "Test content",
            audioFilePath = "/path/to/audio",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val saveResult = saveJournalEntryUseCase(journalEntry)
        assertTrue(saveResult.isSuccess)

        val summary = Summary(
            id = "0",
            entryId = "1", // Assuming the saved entry gets ID "1"
            content = "Test summary",
            bulletPoints = 2,
            inputType = "CONVERSATION",
            createdAt = System.currentTimeMillis(),
            confidence = 0.9f
        )
        val summaryResult = saveSummaryUseCase(summary)
        assertTrue(summaryResult.isSuccess)

        // Verify entry and summary exist
        assertEquals(1, getJournalEntriesUseCase().first().size)
        assertEquals(1, getSummariesForEntryUseCase("1").first().size)

        // When
        val deleteResult = deleteJournalEntryWithSummariesUseCase("1")

        // Then
        assertTrue(deleteResult.isSuccess)
        assertEquals(0, getJournalEntriesUseCase().first().size)
        assertEquals(0, getSummariesForEntryUseCase("1").first().size)
    }
} 