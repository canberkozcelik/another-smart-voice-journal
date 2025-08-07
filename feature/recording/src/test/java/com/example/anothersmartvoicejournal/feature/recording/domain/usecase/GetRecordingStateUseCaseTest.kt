package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.model.RecordingState
import com.example.anothersmartvoicejournal.feature.recording.data.repository.RecordingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetRecordingStateUseCaseTest {

    private lateinit var getRecordingStateUseCase: GetRecordingStateUseCase
    private lateinit var mockRecordingRepository: RecordingRepository

    @Before
    fun setUp() {
        mockRecordingRepository = mockk()
        getRecordingStateUseCase = GetRecordingStateUseCase(mockRecordingRepository)
    }

    @Test
    fun `invoke should return current recording state from repository`() = runTest {
        // Given
        val expectedState = RecordingState(
            isRecording = true,
            duration = 3000L,
            filePath = "/test/path/recording.mp3",
            error = null
        )
        coEvery { mockRecordingRepository.getCurrentRecordingState() } returns flowOf(expectedState)

        // When
        val result = getRecordingStateUseCase().first()

        // Then
        assertEquals(expectedState, result)
        assertTrue(result.isRecording)
        assertEquals(3000L, result.duration)
        assertEquals("/test/path/recording.mp3", result.filePath)
        assertNull(result.error)
    }

    @Test
    fun `invoke should return error state from repository`() = runTest {
        // Given
        val expectedState = RecordingState(
            isRecording = false,
            duration = 0L,
            filePath = null,
            error = "Recording failed"
        )
        coEvery { mockRecordingRepository.getCurrentRecordingState() } returns flowOf(expectedState)

        // When
        val result = getRecordingStateUseCase().first()

        // Then
        assertEquals(expectedState, result)
        assertFalse(result.isRecording)
        assertEquals(0L, result.duration)
        assertNull(result.filePath)
        assertEquals("Recording failed", result.error)
    }
}
