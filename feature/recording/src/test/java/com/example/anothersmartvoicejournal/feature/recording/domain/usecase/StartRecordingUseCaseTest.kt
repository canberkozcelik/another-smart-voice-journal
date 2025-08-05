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

class StartRecordingUseCaseTest {

    private lateinit var startRecordingUseCase: StartRecordingUseCase
    private lateinit var mockRecordingRepository: RecordingRepository

    @Before
    fun setUp() {
        mockRecordingRepository = mockk()
        startRecordingUseCase = StartRecordingUseCase(mockRecordingRepository)
    }

    @Test
    fun `invoke should return recording state from repository`() = runTest {
        // Given
        val expectedState = RecordingState(
            isRecording = true,
            duration = 0L,
            filePath = "/test/path/recording.mp3",
            error = null
        )
        coEvery { mockRecordingRepository.startRecording() } returns flowOf(expectedState)

        // When
        val result = startRecordingUseCase().first()

        // Then
        assertEquals(expectedState, result)
        assertTrue(result.isRecording)
        assertEquals(0L, result.duration)
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
            error = "Permission denied"
        )
        coEvery { mockRecordingRepository.startRecording() } returns flowOf(expectedState)

        // When
        val result = startRecordingUseCase().first()

        // Then
        assertEquals(expectedState, result)
        assertFalse(result.isRecording)
        assertEquals(0L, result.duration)
        assertNull(result.filePath)
        assertEquals("Permission denied", result.error)
    }
}
