package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.recording.domain.PlaybackState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PausePlaybackUseCaseTest {

    private lateinit var useCase: PausePlaybackUseCase
    private lateinit var mockRepository: PlaybackRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = PausePlaybackUseCase(mockRepository)
    }

    @Test
    fun `PausePlaybackUseCase should pause from playing state`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 5000L
        val currentState = PlaybackState.Playing(audioFilePath, currentPosition)
        coEvery { mockRepository.pausePlayback() } returns Result.success(Unit)
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Paused)
        assertEquals(audioFilePath, (result as PlaybackState.Paused).audioFilePath)
        assertEquals(currentPosition, (result as PlaybackState.Paused).currentPosition)
    }

    @Test
    fun `PausePlaybackUseCase should not pause from idle state`() = runTest {
        // Given
        val currentState = PlaybackState.Idle
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Error)
        assertEquals("Cannot pause: not currently playing", (result as PlaybackState.Error).message)
    }

    @Test
    fun `PausePlaybackUseCase should not pause from already paused state`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 10000L
        val currentState = PlaybackState.Paused(audioFilePath, currentPosition)
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Error)
        assertEquals("Cannot pause: already paused", (result as PlaybackState.Error).message)
    }

    @Test
    fun `PausePlaybackUseCase should handle error state gracefully`() = runTest {
        // Given
        val errorState = PlaybackState.Error("Previous error occurred")
        
        // When
        val result = useCase.execute(errorState)
        
        // Then
        assertTrue(result is PlaybackState.Error)
        assertEquals("Cannot pause: not currently playing", (result as PlaybackState.Error).message)
    }

    @Test
    fun `PausePlaybackUseCase should preserve exact position when pausing`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val precisePosition = 12345L
        val currentState = PlaybackState.Playing(audioFilePath, precisePosition)
        coEvery { mockRepository.pausePlayback() } returns Result.success(Unit)
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Paused)
        assertEquals(precisePosition, (result as PlaybackState.Paused).currentPosition)
    }

    @Test
    fun `PausePlaybackUseCase should handle pause failure`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 5000L
        val currentState = PlaybackState.Playing(audioFilePath, currentPosition)
        val errorMessage = "MediaPlayer error"
        coEvery { mockRepository.pausePlayback() } returns Result.failure(RuntimeException(errorMessage))
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Error)
        assertTrue((result as PlaybackState.Error).message.contains(errorMessage))
    }
}
