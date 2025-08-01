package com.example.anothersmartvoicejournal.core.data.repository

import com.example.anothersmartvoicejournal.core.data.dao.JournalDao
import com.example.anothersmartvoicejournal.core.data.entity.JournalEntry
import com.example.anothersmartvoicejournal.core.domain.model.JournalEntry as DomainJournalEntry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class JournalRepositoryImplTest {

    @MockK
    private lateinit var journalDao: JournalDao
    private lateinit var journalRepository: JournalRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        journalRepository = JournalRepositoryImpl(journalDao)
    }

    @Test
    fun `getAllEntries should return mapped domain models`() = runTest {
        // Given
        val entity1 = JournalEntry(
            id = "1",
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
        val entity2 = JournalEntry(
            id = "2",
            title = "Test Entry 2",
            content = "Test content 2",
            audioFilePath = "/path/to/audio2.mp3",
            createdAt = 1234567891L,
            updatedAt = 1234567891L,
            duration = 120000L,
            language = "en",
            transcriptionConfidence = 0.88f,
            isDraft = false
        )

        coEvery { journalDao.getAllEntries() } returns flowOf(listOf(entity1, entity2))

        // When
        val result = journalRepository.getAllEntries().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Test Entry 1", result[0].title)
        assertEquals("Test content 1", result[0].content)
        assertEquals("/path/to/audio1.mp3", result[0].audioFilePath)
        assertEquals(1234567890L, result[0].createdAt)
        assertEquals(1234567890L, result[0].updatedAt)
        assertEquals(60000L, result[0].duration)
        assertEquals("en", result[0].language)
        assertEquals(0.95f, result[0].transcriptionConfidence)
        assertFalse(result[0].isDraft)

        assertEquals("2", result[1].id)
        assertEquals("Test Entry 2", result[1].title)
        assertFalse(result[1].isDraft)
    }

    @Test
    fun `getPublishedEntries should return only non-draft entries`() = runTest {
        // Given
        val entity1 = JournalEntry(
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

        coEvery { journalDao.getPublishedEntries() } returns flowOf(listOf(entity1))

        // When
        val result = journalRepository.getPublishedEntries().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Published Entry", result[0].title)
        assertFalse(result[0].isDraft)
    }

    @Test
    fun `getEntryById should return mapped domain model`() = runTest {
        // Given
        val entity = JournalEntry(
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

        coEvery { journalDao.getEntryById("1") } returns entity

        // When
        val result = journalRepository.getEntryById("1")

        // Then
        assertNotNull(result)
        assertEquals("1", result!!.id)
        assertEquals("Test Entry", result.title)
        assertEquals("Test content", result.content)
    }

    @Test
    fun `getEntryById should return null when entry not found`() = runTest {
        // Given
        coEvery { journalDao.getEntryById("999") } returns null

        // When
        val result = journalRepository.getEntryById("999")

        // Then
        assertNull(result)
    }

    @Test
    fun `saveEntry should call dao insert method`() = runTest {
        // Given
        val domainEntry = DomainJournalEntry(
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

        coEvery { journalDao.insertEntry(any()) } just Runs

        // When
        val result = journalRepository.saveEntry(domainEntry)

        // Then
        coVerify { journalDao.insertEntry(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteEntry should call dao delete method`() = runTest {
        // Given
        val entryId = "1"

        coEvery { journalDao.deleteEntryById(entryId) } just Runs

        // When
        val result = journalRepository.deleteEntry(entryId)

        // Then
        coVerify { journalDao.deleteEntryById(entryId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `searchEntries should return filtered results`() = runTest {
        // Given
        val entity = JournalEntry(
            id = "1",
            title = "Searchable Entry",
            content = "This contains the search term",
            audioFilePath = "/path/to/audio.mp3",
            createdAt = 1234567890L,
            updatedAt = 1234567890L,
            duration = 60000L,
            language = "en",
            transcriptionConfidence = 0.95f,
            isDraft = false
        )

        coEvery { journalDao.searchEntries("search") } returns flowOf(listOf(entity))

        // When
        val result = journalRepository.searchEntries("search").first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Searchable Entry", result[0].title)
    }

    @Test
    fun `getEntryCount should return correct count`() = runTest {
        // Given
        coEvery { journalDao.getEntryCount() } returns flowOf(5)

        // When
        val result = journalRepository.getEntryCount().first()

        // Then
        assertEquals(5, result)
    }
}
