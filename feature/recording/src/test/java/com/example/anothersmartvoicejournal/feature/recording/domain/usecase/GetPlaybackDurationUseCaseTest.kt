package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPlaybackDurationUseCaseTest {

    private lateinit var useCase: GetPlaybackDurationUseCase
    private lateinit var mockRepository: PlaybackRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetPlaybackDurationUseCase(mockRepository)
    }

    @Test
    fun `GetPlaybackDurationUseCase should return duration for valid audio file`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val expectedDuration = 120000L // 2 minutes in milliseconds
        coEvery { mockRepository.getAudioDuration(audioFilePath) } returns expectedDuration
        
        // When
        val result = useCase.execute(audioFilePath)
        
        // Then
        assertEquals(expectedDuration, result)
    }

    @Test
    fun `GetPlaybackDurationUseCase should handle empty file path`() = runTest {
        // Given
        val emptyPath = ""
        
        // When
        val result = useCase.execute(emptyPath)
        
        // Then
        assertEquals(-1L, result)
    }

    @Test
    fun `GetPlaybackDurationUseCase should handle blank file path`() = runTest {
        // Given
        val blankPath = "   "
        
        // When
        val result = useCase.execute(blankPath)
        
        // Then
        assertEquals(-1L, result)
    }

    @Test
    fun `GetPlaybackDurationUseCase should handle null file path`() = runTest {
        // Given
        val nullPath: String? = null
        
        // When
        val result = useCase.execute(nullPath)
        
        // Then
        assertEquals(-1L, result)
    }

    @Test
    fun `GetPlaybackDurationUseCase should return zero for empty audio file`() = runTest {
        // Given
        val emptyAudioPath = "/test/empty.mp3"
        coEvery { mockRepository.getAudioDuration(emptyAudioPath) } returns 0L
        
        // When
        val result = useCase.execute(emptyAudioPath)
        
        // Then
        assertEquals(0L, result)
    }

    @Test
    fun `GetPlaybackDurationUseCase should handle very short audio files`() = runTest {
        // Given
        val shortAudioPath = "/test/short.mp3"
        val expectedDuration = 100L // 100ms
        coEvery { mockRepository.getAudioDuration(shortAudioPath) } returns expectedDuration
        
        // When
        val result = useCase.execute(shortAudioPath)
        
        // Then
        assertEquals(expectedDuration, result)
    }

    @Test
    fun `GetPlaybackDurationUseCase should handle very long audio files`() = runTest {
        // Given
        val longAudioPath = "/test/long.mp3"
        val expectedDuration = 7200000L // 2 hours in milliseconds
        coEvery { mockRepository.getAudioDuration(longAudioPath) } returns expectedDuration
        
        // When
        val result = useCase.execute(longAudioPath)
        
        // Then
        assertEquals(expectedDuration, result)
    }
}
