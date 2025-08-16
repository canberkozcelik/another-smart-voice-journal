package com.example.anothersmartvoicejournal.feature.journal.domain.usecase

import com.example.anothersmartvoicejournal.feature.journal.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.StopPlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
        val result = useCase.execute()

        // Then
        Assert.assertTrue(result is PlaybackState.Idle)
    }

    @Test
    fun `StopPlaybackUseCase should be consistent across multiple calls`() = runTest {
        // Given
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)

        // When & Then
        repeat(5) {
            val result = useCase.execute()
            Assert.assertTrue("Call $it should return Idle", result is PlaybackState.Idle)
        }
    }

    @Test
    fun `StopPlaybackUseCase should handle stop failure`() = runTest {
        // Given
        val errorMessage = "MediaPlayer error"
        coEvery { mockRepository.stopPlayback() } returns Result.failure(RuntimeException(errorMessage))

        // When
        val result = useCase.execute()

        // Then
        Assert.assertTrue(result is PlaybackState.Error)
        Assert.assertTrue((result as PlaybackState.Error).message.contains(errorMessage))
    }

    @Test
    fun `StopPlaybackUseCase should stop from playing state`() = runTest {
        // Given
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)

        // When
        val result = useCase.execute()

        // Then
        Assert.assertTrue(result is PlaybackState.Idle)
    }

    @Test
    fun `StopPlaybackUseCase should stop from paused state`() = runTest {
        // Given
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)

        // When
        val result = useCase.execute()

        // Then
        Assert.assertTrue(result is PlaybackState.Idle)
    }

    @Test
    fun `StopPlaybackUseCase should stop from error state`() = runTest {
        // Given
        coEvery { mockRepository.stopPlayback() } returns Result.success(Unit)

        // When
        val result = useCase.execute()

        // Then
        Assert.assertTrue(result is PlaybackState.Idle)
    }
}
