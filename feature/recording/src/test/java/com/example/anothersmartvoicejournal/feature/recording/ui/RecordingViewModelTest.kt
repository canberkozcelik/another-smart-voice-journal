package com.example.anothersmartvoicejournal.feature.recording.ui

import com.example.anothersmartvoicejournal.feature.recording.data.model.RecordingState
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.GetRecordingDurationUseCase
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.GetRecordingStateUseCase
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.StartRecordingUseCase
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.StopRecordingUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecordingViewModelTest {

    private lateinit var viewModel: RecordingViewModel
    private lateinit var startRecordingUseCase: StartRecordingUseCase
    private lateinit var stopRecordingUseCase: StopRecordingUseCase

    private lateinit var getRecordingStateUseCase: GetRecordingStateUseCase
    private lateinit var getRecordingDurationUseCase: GetRecordingDurationUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        startRecordingUseCase = mockk()
        stopRecordingUseCase = mockk()

        getRecordingStateUseCase = mockk()
        getRecordingDurationUseCase = mockk()

        // Set up default mocks for init block calls
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        viewModel = RecordingViewModel(
            startRecordingUseCase,
            stopRecordingUseCase,
            getRecordingStateUseCase,
            getRecordingDurationUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(
        recordingState: RecordingState = RecordingState(),
        duration: Long = 0L
    ): RecordingViewModel {
        coEvery { getRecordingStateUseCase() } returns flowOf(recordingState)
        coEvery { getRecordingDurationUseCase() } returns flowOf(duration)

        return RecordingViewModel(
            startRecordingUseCase,
            stopRecordingUseCase,
            getRecordingStateUseCase,
            getRecordingDurationUseCase
        )
    }

    private fun createViewModelAndWait(
        recordingState: RecordingState = RecordingState(),
        duration: Long = 0L
    ): RecordingViewModel {
        val testViewModel = createViewModel(recordingState, duration)
        testDispatcher.scheduler.advanceUntilIdle() // Wait for flows to be collected
        return testViewModel
    }

    @Test
    fun `initial state should be correct`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        val initialState = viewModel.uiState.value

        // Then
        assertFalse(initialState.isLoading)
        assertFalse(initialState.isRecording)
        assertEquals(0L, initialState.duration)
        assertNull(initialState.filePath)
        assertNull(initialState.error)
    }

    @Test
    fun `startRecording should update state correctly`() = runTest(testDispatcher) {
        // Given
        val recordingState = RecordingState(
            isRecording = true,
            duration = 0L, // Duration starts at 0 when recording starts
            filePath = "/test/path/recording.mp3"
        )
        coEvery { startRecordingUseCase() } returns flowOf(recordingState)

        // When
        viewModel.startRecording()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isRecording)
        assertEquals(0L, state.duration) // Duration should be 0 when starting
        assertEquals("/test/path/recording.mp3", state.filePath)
        assertNull(state.error)
    }

    @Test
    fun `stopRecording should update state correctly`() = runTest(testDispatcher) {
        // Given
        val recordingState = RecordingState(
            isRecording = false,
            duration = 10000L,
            filePath = "/test/path/recording.mp3"
        )
        coEvery { stopRecordingUseCase() } returns flowOf(recordingState)

        // When
        viewModel.stopRecording()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isRecording)
        assertEquals(10000L, state.duration)
        assertEquals("/test/path/recording.mp3", state.filePath)
        assertNull(state.error)
    }

    @Test
    fun `clearError should clear error state`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState(error = "Test error"))
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.clearError()

        // Then
        val state = viewModel.uiState.value
        assertNull(state.error)
    }

    @Test
    fun `formattedDuration should format correctly`() = runTest(testDispatcher) {
        // Given
        val recordingState = RecordingState(duration = 65000L) // 65 seconds
        coEvery { stopRecordingUseCase() } returns flowOf(recordingState)

        // When
        viewModel.stopRecording()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value

        // Then
        assertEquals("01:05", state.formattedDuration)
    }

    @Test
    fun `canStartRecording should be true when permission is not requested initially`() = runTest(testDispatcher) {
        // Given
        val testViewModel = createViewModelAndWait()

        // When
        val state = testViewModel.uiState.value

        // Then
        assertTrue(state.canStartRecording) // Should be true to allow permission requests
    }

    @Test
    fun `canStopRecording should be true when recording`() = runTest(testDispatcher) {
        // Given
        val recordingState = RecordingState(isRecording = true)
        coEvery { startRecordingUseCase() } returns flowOf(recordingState)

        // When
        viewModel.startRecording()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.canStopRecording)
    }

    // Permission-related tests
    @Test
    fun `initial permission state should be NotRequested`() = runTest(testDispatcher) {
        // Given
        val testViewModel = createViewModelAndWait()

        // When
        val initialState = testViewModel.uiState.value

        // Then
        assertEquals(PermissionState.NotRequested, initialState.permissionState)
    }

    @Test
    fun `updatePermissionState should update permission state correctly`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.updatePermissionState(PermissionState.Granted)

        // Then
        val state = viewModel.uiState.value
        assertEquals(PermissionState.Granted, state.permissionState)
    }

    @Test
    fun `canStartRecording should return false when permission is not granted`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.updatePermissionState(PermissionState.Denied)
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.canStartRecording)
    }

    @Test
    fun `canStartRecording should return true when permission is granted and not recording`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.updatePermissionState(PermissionState.Granted)
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.canStartRecording)
    }

    @Test
    fun `needsPermission should return true when permission is not granted`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.updatePermissionState(PermissionState.Denied)
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.needsPermission)
    }

    @Test
    fun `needsPermission should return false when permission is granted`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.updatePermissionState(PermissionState.Granted)
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.needsPermission)
    }

    @Test
    fun `showPermissionError should return true when permission is permanently denied`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.updatePermissionState(PermissionState.PermanentlyDenied)
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.showPermissionError)
    }

    @Test
    fun `showPermissionError should return false when permission is not permanently denied`() = runTest(testDispatcher) {
        // Given
        coEvery { getRecordingStateUseCase() } returns flowOf(RecordingState())
        coEvery { getRecordingDurationUseCase() } returns flowOf(0L)

        // When
        viewModel.updatePermissionState(PermissionState.Granted)
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.showPermissionError)
    }
}
