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
import org.junit.Before
import org.junit.Test

class StopRecordingUseCaseTest {

    private lateinit var stopRecordingUseCase: StopRecordingUseCase
    private lateinit var mockRecordingRepository: RecordingRepository

    @Before
    fun setUp() {
        mockRecordingRepository = mockk()
        stopRecordingUseCase = StopRecordingUseCase(mockRecordingRepository)
    }

    @Test
    fun `invoke should return stopped recording state from repository`() = runTest {
        // Given
        val expectedState = RecordingState(
            isRecording = false,
            duration = 5000L,
            filePath = "/test/path/recording.mp3",
            error = null
        )
        coEvery { mockRecordingRepository.stopRecording() } returns flowOf(expectedState)

        // When
        val result = stopRecordingUseCase().first()

        // Then
        assertEquals(expectedState, result)
        assertFalse(result.isRecording)
        assertEquals(5000L, result.duration)
        assertEquals("/test/path/recording.mp3", result.filePath)
        assertNull(result.error)
    }

    @Test
    fun `invoke should handle error state from repository`() = runTest {
        // Given
        val expectedState = RecordingState(
            isRecording = false,
            duration = 0L,
            filePath = null,
            error = "Failed to stop recording"
        )
        coEvery { mockRecordingRepository.stopRecording() } returns flowOf(expectedState)

        // When
        val result = stopRecordingUseCase().first()

        // Then
        assertEquals(expectedState, result)
        assertFalse(result.isRecording)
        assertEquals(0L, result.duration)
        assertNull(result.filePath)
        assertEquals("Failed to stop recording", result.error)
    }
}
