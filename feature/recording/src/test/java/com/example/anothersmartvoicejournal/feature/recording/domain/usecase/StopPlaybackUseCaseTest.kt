package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.recording.domain.PlaybackState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StopPlaybackUseCaseTest {

    private lateinit var useCase: StopPlaybackUseCase
    private lateinit var mockRepository: PlaybackRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = StopPlaybackUseCase(mockRepository)
    }

    @Test
    fun `StopPlaybackUseCase should always return idle state regardless of input`() = runTest {
        // Given
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)
        
        // When
        val result = useCase.execute(PlaybackState.Idle)
        
        // Then
        assertTrue(result is PlaybackState.Idle)
    }

    @Test
    fun `StopPlaybackUseCase should be consistent across multiple calls`() = runTest {
        // Given
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)
        
        // When & Then
        repeat(5) {
            val result = useCase.execute(PlaybackState.Idle)
            assertTrue("Call $it should return Idle", result is PlaybackState.Idle)
        }
    }

    @Test
    fun `StopPlaybackUseCase should handle stop failure`() = runTest {
        // Given
        val errorMessage = "MediaPlayer error"
        coEvery { mockRepository.stopPlayback() } returns Result.failure(RuntimeException(errorMessage))
        
        // When
        val result = useCase.execute(PlaybackState.Playing("/test/audio.mp3", 5000L))
        
        // Then
        assertTrue(result is PlaybackState.Error)
        assertTrue((result as PlaybackState.Error).message.contains(errorMessage))
    }

    @Test
    fun `StopPlaybackUseCase should stop from playing state`() = runTest {
        // Given
        val currentState = PlaybackState.Playing("/test/audio.mp3", 5000L)
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Idle)
    }

    @Test
    fun `StopPlaybackUseCase should stop from paused state`() = runTest {
        // Given
        val currentState = PlaybackState.Paused("/test/audio.mp3", 10000L)
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Idle)
    }

    @Test
    fun `StopPlaybackUseCase should stop from error state`() = runTest {
        // Given
        val currentState = PlaybackState.Error("Previous error occurred")
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)
        
        // When
        val result = useCase.execute(currentState)
        
        // Then
        assertTrue(result is PlaybackState.Idle)
    }
}
