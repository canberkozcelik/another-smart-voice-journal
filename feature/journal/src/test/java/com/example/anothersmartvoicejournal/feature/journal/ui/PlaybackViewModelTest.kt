package com.example.anothersmartvoicejournal.feature.journal.ui

import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.GetPlaybackDurationUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.GetPlaybackPositionUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.PausePlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.StartPlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.StopPlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import com.example.anothersmartvoicejournal.feature.journal.ui.playback.PlaybackViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaybackViewModelTest {

    private lateinit var viewModel: PlaybackViewModel
    private lateinit var startPlaybackUseCase: StartPlaybackUseCase
    private lateinit var pausePlaybackUseCase: PausePlaybackUseCase
    private lateinit var stopPlaybackUseCase: StopPlaybackUseCase
    private lateinit var getPlaybackDurationUseCase: GetPlaybackDurationUseCase
    private lateinit var getPlaybackPositionUseCase: GetPlaybackPositionUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        startPlaybackUseCase = mockk()
        pausePlaybackUseCase = mockk()
        stopPlaybackUseCase = mockk()
        getPlaybackDurationUseCase = mockk()
        getPlaybackPositionUseCase = mockk()

        viewModel = PlaybackViewModel(
            startPlaybackUseCase,
            pausePlaybackUseCase,
            stopPlaybackUseCase,
            getPlaybackDurationUseCase,
            getPlaybackPositionUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be correct`() = runTest(testDispatcher) {
        // When
        val initialState = viewModel.uiState.value

        // Then
        Assert.assertFalse(initialState.isLoading)
        assertEquals(PlaybackState.Idle, initialState.playbackState)
        Assert.assertNull(initialState.error)
        Assert.assertEquals(0L, initialState.duration)
        Assert.assertEquals(0L, initialState.currentPosition)
    }

    @Test
    fun `startPlayback should update state correctly`() = runTest(testDispatcher) {
        // Given
        val audioFilePath = "/test/path/audio.mp3"
        val newState = PlaybackState.Playing(audioFilePath, 0L)
        coEvery { startPlaybackUseCase.execute(audioFilePath, any()) } returns newState

        // When
        viewModel.startPlayback(audioFilePath)

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        Assert.assertFalse(currentState.isLoading)
        assertEquals(newState, currentState.playbackState)
        Assert.assertNull(currentState.error)
    }

    @Test
    fun `startPlayback should complete loading and update state`() = runTest(testDispatcher) {
        // Given
        val audioFilePath = "/test/path/audio.mp3"
        val expectedState = PlaybackState.Playing(audioFilePath, 0L)
        coEvery { startPlaybackUseCase.execute(audioFilePath, any()) } returns expectedState

        // When
        viewModel.startPlayback(audioFilePath)

        // Then - After completion, loading should be false and state updated
        testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(expectedState, viewModel.uiState.value.playbackState)
    }

    @Test
    fun `startPlayback should handle errors`() = runTest(testDispatcher) {
        // Given
        val audioFilePath = "/test/path/audio.mp3"
        val errorState = PlaybackState.Error("Failed to start playback", PlaybackState.Idle)
        coEvery { startPlaybackUseCase.execute(audioFilePath, any()) } returns errorState

        // When
        viewModel.startPlayback(audioFilePath)

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        assertEquals(errorState, currentState.playbackState)
        Assert.assertFalse(currentState.isLoading)
    }

    @Test
    fun `pausePlayback should update state correctly`() = runTest(testDispatcher) {
        // Given
        val audioFilePath = "/test/path/audio.mp3"
        val currentPosition = 5000L
        val pausedState = PlaybackState.Paused(audioFilePath, currentPosition)
        coEvery { pausePlaybackUseCase.execute(any()) } returns pausedState

        // When
        viewModel.pausePlayback()

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        assertEquals(pausedState, currentState.playbackState)
        Assert.assertFalse(currentState.isLoading)
    }

    @Test
    fun `pausePlayback should complete loading and update state`() = runTest(testDispatcher) {
        // Given
        val expectedState = PlaybackState.Paused("/test/path/audio.mp3", 5000L)
        coEvery { pausePlaybackUseCase.execute(any()) } returns expectedState

        // When
        viewModel.pausePlayback()

        // Then - After completion, loading should be false and state updated
        testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(expectedState, viewModel.uiState.value.playbackState)
    }

    @Test
    fun `pausePlayback should handle errors`() = runTest(testDispatcher) {
        // Given
        val errorState = PlaybackState.Error("Cannot pause when not playing", PlaybackState.Idle)
        coEvery { pausePlaybackUseCase.execute(any()) } returns errorState

        // When
        viewModel.pausePlayback()

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        assertEquals(errorState, currentState.playbackState)
        Assert.assertFalse(currentState.isLoading)
    }

    @Test
    fun `stopPlayback should update state correctly`() = runTest(testDispatcher) {
        // Given
        coEvery { stopPlaybackUseCase.execute() } returns PlaybackState.Idle

        // When
        viewModel.stopPlayback()

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        assertEquals(PlaybackState.Idle, currentState.playbackState)
        Assert.assertFalse(currentState.isLoading)
    }

    @Test
    fun `stopPlayback should complete loading and update state`() = runTest(testDispatcher) {
        // Given
        coEvery { stopPlaybackUseCase.execute() } returns PlaybackState.Idle

        // When
        viewModel.stopPlayback()

        // Then - After completion, loading should be false and state updated
        testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(PlaybackState.Idle, viewModel.uiState.value.playbackState)
    }

    @Test
    fun `stopPlayback should handle errors`() = runTest(testDispatcher) {
        // Given
        val errorState = PlaybackState.Error("Failed to stop playback", PlaybackState.Idle)
        coEvery { stopPlaybackUseCase.execute() } returns errorState

        // When
        viewModel.stopPlayback()

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        assertEquals(errorState, currentState.playbackState)
        Assert.assertFalse(currentState.isLoading)
    }

    @Test
    fun `getDuration should update duration state`() = runTest(testDispatcher) {
        // Given
        val audioFilePath = "/test/path/audio.mp3"
        val expectedDuration = 30000L
        coEvery { getPlaybackDurationUseCase.execute(audioFilePath) } returns expectedDuration

        // When
        viewModel.getDuration(audioFilePath)

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        Assert.assertEquals(expectedDuration, currentState.duration)
        Assert.assertFalse(currentState.isLoading)
    }

    @Test
    fun `getDuration should complete loading and update duration`() = runTest(testDispatcher) {
        // Given
        val audioFilePath = "/test/path/audio.mp3"
        val expectedDuration = 30000L
        coEvery { getPlaybackDurationUseCase.execute(audioFilePath) } returns expectedDuration

        // When
        viewModel.getDuration(audioFilePath)

        // Then - After completion, loading should be false and duration updated
        testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertEquals(expectedDuration, viewModel.uiState.value.duration)
    }

    @Test
    fun `getCurrentPosition should update position state`() = runTest(testDispatcher) {
        // Given
        val expectedPosition = 15000L
        coEvery { getPlaybackPositionUseCase.execute() } returns expectedPosition

        // When
        viewModel.getCurrentPosition()

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val currentState = viewModel.uiState.value
        Assert.assertEquals(expectedPosition, currentState.currentPosition)
        Assert.assertFalse(currentState.isLoading)
    }

    @Test
    fun `getCurrentPosition should complete loading and update position`() = runTest(testDispatcher) {
        // Given
        val expectedPosition = 15000L
        coEvery { getPlaybackPositionUseCase.execute() } returns expectedPosition

        // When
        viewModel.getCurrentPosition()

        // Then - After completion, loading should be false and position updated
        testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertFalse(viewModel.uiState.value.isLoading)
        Assert.assertEquals(expectedPosition, viewModel.uiState.value.currentPosition)
    }

    @Test
    fun `clearError should clear error state`() = runTest(testDispatcher) {
        // Given
        val errorState = PlaybackState.Error("Test error", PlaybackState.Idle)
        coEvery { startPlaybackUseCase.execute("/test/path/audio.mp3", any()) } returns errorState

        // Set error state first
        viewModel.startPlayback("/test/path/audio.mp3")
        testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertTrue(viewModel.uiState.value.playbackState is PlaybackState.Error)

        // When
        viewModel.clearError()

        // Then
        val currentState = viewModel.uiState.value
        assertEquals(PlaybackState.Idle, currentState.playbackState)
    }

    @Test
    fun `showError should set error state`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Custom error message"

        // When
        viewModel.showError(errorMessage)

        // Then
        val currentState = viewModel.uiState.value
        Assert.assertTrue(currentState.playbackState is PlaybackState.Error)
        Assert.assertEquals(
            errorMessage,
            (currentState.playbackState as PlaybackState.Error).message
        )
    }

    @Test
    fun `state transitions should work correctly`() = runTest(testDispatcher) {
        // Given - Start with idle
        assertEquals(PlaybackState.Idle, viewModel.uiState.value.playbackState)

        // When - Start playback
        val audioFilePath = "/test/path/audio.mp3"
        val playingState = PlaybackState.Playing(audioFilePath, 0L)
        coEvery { startPlaybackUseCase.execute(audioFilePath, any()) } returns playingState

        viewModel.startPlayback(audioFilePath)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should be playing
        assertEquals(playingState, viewModel.uiState.value.playbackState)

        // When - Pause playback
        val pausedState = PlaybackState.Paused(audioFilePath, 5000L)
        coEvery { pausePlaybackUseCase.execute(any()) } returns pausedState

        viewModel.pausePlayback()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should be paused
        assertEquals(pausedState, viewModel.uiState.value.playbackState)

        // When - Stop playback
        coEvery { stopPlaybackUseCase.execute() } returns PlaybackState.Idle

        viewModel.stopPlayback()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should be idle
        assertEquals(PlaybackState.Idle, viewModel.uiState.value.playbackState)
    }
}
