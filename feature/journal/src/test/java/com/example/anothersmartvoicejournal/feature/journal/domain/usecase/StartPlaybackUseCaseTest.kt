package com.example.anothersmartvoicejournal.feature.journal.domain.usecase

import com.example.anothersmartvoicejournal.feature.journal.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.StartPlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StartPlaybackUseCaseTest {

    private lateinit var useCase: StartPlaybackUseCase
    private lateinit var mockRepository: PlaybackRepository

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = StartPlaybackUseCase(mockRepository)
    }

    @Test
    fun `StartPlaybackUseCase should start playing from idle state`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentState = PlaybackState.Idle
        coEvery { mockRepository.startPlayback(audioFilePath) } returns Result.success(Unit)

        // When
        val result = useCase.execute(audioFilePath, currentState)

        // Then
        assertTrue(result is PlaybackState.Playing)
        assertEquals(audioFilePath, (result as PlaybackState.Playing).audioFilePath)
        assertEquals(0L, result.currentPosition)
    }

    @Test
    fun `StartPlaybackUseCase should handle invalid file path`() = runTest {
        // Given
        val invalidPath = ""
        val currentState = PlaybackState.Idle

        // When
        val result = useCase.execute(invalidPath, currentState)

        // Then
        assertTrue(result is PlaybackState.Error)
        assertEquals("Invalid audio file path", (result as PlaybackState.Error).message)
    }

    @Test
    fun `StartPlaybackUseCase should resume from paused state`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 5000L
        val currentState = PlaybackState.Paused(audioFilePath, currentPosition)
        coEvery { mockRepository.startPlayback(audioFilePath) } returns Result.success(Unit)

        // When
        val result = useCase.execute(audioFilePath, currentState)

        // Then
        assertTrue(result is PlaybackState.Playing)
        assertEquals(audioFilePath, (result as PlaybackState.Playing).audioFilePath)
        assertEquals(currentPosition, result.currentPosition)
    }

    @Test
    fun `StartPlaybackUseCase should start new audio when already playing`() = runTest {
        // Given
        val firstPath = "/test/audio1.mp3"
        val secondPath = "/test/audio2.mp3"
        val currentState = PlaybackState.Playing(firstPath, 10000L)
        coEvery { mockRepository.startPlayback(secondPath) } returns Result.success(Unit)

        // When
        val result = useCase.execute(secondPath, currentState)

        // Then
        assertTrue(result is PlaybackState.Playing)
        assertEquals(secondPath, (result as PlaybackState.Playing).audioFilePath)
        assertEquals(0L, result.currentPosition)
    }

    @Test
    fun `StartPlaybackUseCase should handle error state recovery`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val errorState = PlaybackState.Error("Previous error occurred")
        coEvery { mockRepository.startPlayback(audioFilePath) } returns Result.success(Unit)

        // When
        val result = useCase.execute(audioFilePath, errorState)

        // Then
        assertTrue(result is PlaybackState.Playing)
        assertEquals(audioFilePath, (result as PlaybackState.Playing).audioFilePath)
        assertEquals(0L, result.currentPosition)
    }

    @Test
    fun `StartPlaybackUseCase should handle playback start failure`() = runTest {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentState = PlaybackState.Idle
        val errorMessage = "File not found"
        coEvery { mockRepository.startPlayback(audioFilePath) } returns Result.failure(RuntimeException(errorMessage))

        // When
        val result = useCase.execute(audioFilePath, currentState)

        // Then
        assertTrue(result is PlaybackState.Error)
        assertTrue((result as PlaybackState.Error).message.contains(errorMessage))
    }
}
