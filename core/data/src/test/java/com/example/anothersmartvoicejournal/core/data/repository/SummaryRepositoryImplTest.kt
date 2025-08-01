package com.example.anothersmartvoicejournal.core.data.repository

import com.example.anothersmartvoicejournal.core.data.dao.SummaryDao
import com.example.anothersmartvoicejournal.core.data.entity.Summary
import com.example.anothersmartvoicejournal.core.domain.model.Summary as DomainSummary
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SummaryRepositoryImplTest {

    @MockK
    private lateinit var summaryDao: SummaryDao
    private lateinit var summaryRepository: SummaryRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        summaryRepository = SummaryRepositoryImpl(summaryDao)
    }

    @Test
    fun `getSummariesForEntry should return mapped domain models`() = runTest {
        // Given
        val entity1 = Summary(
            id = "1",
            entryId = "entry1",
            content = "• First bullet point\n• Second bullet point",
            bulletPoints = 2,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )
        val entity2 = Summary(
            id = "2",
            entryId = "entry1",
            content = "• Alternative summary",
            bulletPoints = 1,
            inputType = "CONVERSATION",
            createdAt = 1234567891L,
            confidence = 0.88f
        )

        coEvery { summaryDao.getSummariesForEntry("entry1") } returns flowOf(listOf(entity1, entity2))

        // When
        val result = summaryRepository.getSummariesForEntry("entry1").first()

        // Then
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("entry1", result[0].entryId)
        assertEquals("• First bullet point\n• Second bullet point", result[0].content)
        assertEquals(2, result[0].bulletPoints)
        assertEquals("ARTICLE", result[0].inputType)
        assertEquals(0.95f, result[0].confidence)

        assertEquals("2", result[1].id)
        assertEquals("CONVERSATION", result[1].inputType)
    }

    @Test
    fun `getSummaryById should return mapped domain model`() = runTest {
        // Given
        val entity = Summary(
            id = "1",
            entryId = "entry1",
            content = "• Test summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        coEvery { summaryDao.getSummaryById("1") } returns entity

        // When
        val result = summaryRepository.getSummaryById("1")

        // Then
        assertNotNull(result)
        assertEquals("1", result!!.id)
        assertEquals("entry1", result.entryId)
        assertEquals("• Test summary", result.content)
        assertEquals(1, result.bulletPoints)
        assertEquals("ARTICLE", result.inputType)
    }

    @Test
    fun `getSummaryById should return null when summary not found`() = runTest {
        // Given
        coEvery { summaryDao.getSummaryById("999") } returns null

        // When
        val result = summaryRepository.getSummaryById("999")

        // Then
        assertNull(result)
    }

    @Test
    fun `saveSummary should call dao insert method`() = runTest {
        // Given
        val domainSummary = DomainSummary(
            id = "1",
            entryId = "entry1",
            content = "• Test summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        coEvery { summaryDao.insertSummary(any()) } just Runs

        // When
        val result = summaryRepository.saveSummary(domainSummary)

        // Then
        coVerify { summaryDao.insertSummary(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteSummary should call dao delete method`() = runTest {
        // Given
        val summaryId = "1"
        val entity = Summary(
            id = "1",
            entryId = "entry1",
            content = "• Test summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        coEvery { summaryDao.getSummaryById("1") } returns entity
        coEvery { summaryDao.deleteSummary(any()) } just Runs

        // When
        val result = summaryRepository.deleteSummary(summaryId)

        // Then
        coVerify { summaryDao.deleteSummary(entity) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteSummariesForEntry should call dao delete method`() = runTest {
        // Given
        val entryId = "entry1"

        coEvery { summaryDao.deleteSummariesForEntry(entryId) } just Runs

        // When
        val result = summaryRepository.deleteSummariesForEntry(entryId)

        // Then
        coVerify { summaryDao.deleteSummariesForEntry(entryId) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getRecentSummaries should return limited results`() = runTest {
        // Given
        val entity = Summary(
            id = "1",
            entryId = "entry1",
            content = "• Recent summary",
            bulletPoints = 1,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        coEvery { summaryDao.getRecentSummaries(5) } returns flowOf(listOf(entity))

        // When
        val result = summaryRepository.getRecentSummaries(5).first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("• Recent summary", result[0].content)
    }

    @Test
    fun `domain model should have correct bulletPointList`() = runTest {
        // Given
        val entity = Summary(
            id = "1",
            entryId = "entry1",
            content = "• First point\n• Second point\n• Third point",
            bulletPoints = 3,
            inputType = "ARTICLE",
            createdAt = 1234567890L,
            confidence = 0.95f
        )

        coEvery { summaryDao.getSummaryById("1") } returns entity

        // When
        val result = summaryRepository.getSummaryById("1")

        // Then
        assertNotNull(result)
        assertEquals(3, result.bulletPointList.size)
        assertEquals("• First point", result.bulletPointList[0])
        assertEquals("• Second point", result.bulletPointList[1])
        assertEquals("• Third point", result.bulletPointList[2])
    }
}
