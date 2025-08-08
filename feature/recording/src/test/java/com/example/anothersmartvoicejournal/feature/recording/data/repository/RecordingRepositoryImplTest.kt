package com.example.anothersmartvoicejournal.feature.recording.data.repository

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class RecordingRepositoryImplTest {

    private lateinit var recordingRepository: RecordingRepositoryImpl
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        // Use Robolectric's RuntimeEnvironment to get a real Android Context
        mockContext = RuntimeEnvironment.getApplication()
        recordingRepository = RecordingRepositoryImpl(mockContext)
    }

    @Test
    fun `startRecording should return recording state with isRecording true`() = runTest {
        // When
        val result = recordingRepository.startRecording().first()

        // Then
        assertTrue(result.isRecording)
        assertEquals(0L, result.duration)
        assertNotNull(result.filePath)
        assertNull(result.error)
    }

    @Test
    fun `stopRecording should return recording state with isRecording false`() = runTest {
        // Start recording first
        recordingRepository.startRecording().first()

        // When
        val result = recordingRepository.stopRecording().first()

        // Then
        assertFalse(result.isRecording)
        assertTrue(result.duration >= 0L)
        assertNotNull(result.filePath)
        assertNull(result.error)
    }

    @Test
    fun `isRecording should return false initially`() {
        // When
        val result = recordingRepository.isRecording()

        // Then
        assertFalse(result)
    }

    @Test
    fun `getRecordingFilePath should return null initially`() {
        // When
        val result = recordingRepository.getRecordingFilePath()

        // Then
        assertNull(result)
    }

    @Test
    fun `getCurrentRecordingState should return initial state`() = runTest {
        // When
        val result = recordingRepository.getCurrentRecordingState().first()

        // Then
        assertFalse(result.isRecording)
        assertEquals(0L, result.duration)
        assertNull(result.filePath)
        assertNull(result.error)
    }

    @Test
    fun `getRecordingDuration should return initial duration`() = runTest {
        // When
        val result = recordingRepository.getRecordingDuration().first()

        // Then
        assertEquals(0L, result)
    }
}
