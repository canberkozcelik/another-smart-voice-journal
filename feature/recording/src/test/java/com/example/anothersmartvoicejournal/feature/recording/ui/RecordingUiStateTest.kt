package com.example.anothersmartvoicejournal.feature.recording.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecordingUiStateTest {

    @Test
    fun `initial state should have NotRequested permission`() {
        // When
        val uiState = RecordingUiState()

        // Then
        assertEquals(PermissionState.NotRequested, uiState.permissionState)
    }

    @Test
    fun `formattedDuration should format correctly`() {
        // Given
        val uiState = RecordingUiState(duration = 65000L) // 65 seconds

        // When
        val formatted = uiState.formattedDuration

        // Then
        assertEquals("01:05", formatted)
    }

    @Test
    fun `formattedDuration should handle zero duration`() {
        // Given
        val uiState = RecordingUiState(duration = 0L)

        // When
        val formatted = uiState.formattedDuration

        // Then
        assertEquals("00:00", formatted)
    }

    @Test
    fun `canStartRecording should be false when permission is not granted`() {
        // Given
        val uiState = RecordingUiState(
            isLoading = false,
            isRecording = false,
            permissionState = PermissionState.Denied
        )

        // When
        val canStart = uiState.canStartRecording

        // Then
        assertFalse(canStart)
    }

    @Test
    fun `canStartRecording should be true when permission is granted and not recording`() {
        // Given
        val uiState = RecordingUiState(
            isLoading = false,
            isRecording = false,
            permissionState = PermissionState.Granted
        )

        // When
        val canStart = uiState.canStartRecording

        // Then
        assertTrue(canStart)
    }

    @Test
    fun `canStartRecording should be false when loading`() {
        // Given
        val uiState = RecordingUiState(
            isLoading = true,
            isRecording = false,
            permissionState = PermissionState.Granted
        )

        // When
        val canStart = uiState.canStartRecording

        // Then
        assertFalse(canStart)
    }

    @Test
    fun `canStartRecording should be false when recording`() {
        // Given
        val uiState = RecordingUiState(
            isLoading = false,
            isRecording = true,
            permissionState = PermissionState.Granted
        )

        // When
        val canStart = uiState.canStartRecording

        // Then
        assertFalse(canStart)
    }

    @Test
    fun `canStopRecording should be true when recording`() {
        // Given
        val uiState = RecordingUiState(
            isLoading = false,
            isRecording = true
        )

        // When
        val canStop = uiState.canStopRecording

        // Then
        assertTrue(canStop)
    }

    @Test
    fun `canStopRecording should be false when not recording`() {
        // Given
        val uiState = RecordingUiState(
            isLoading = false,
            isRecording = false
        )

        // When
        val canStop = uiState.canStopRecording

        // Then
        assertFalse(canStop)
    }

    @Test
    fun `canStopRecording should be false when loading`() {
        // Given
        val uiState = RecordingUiState(
            isLoading = true,
            isRecording = true
        )

        // When
        val canStop = uiState.canStopRecording

        // Then
        assertFalse(canStop)
    }

    @Test
    fun `hasError should be true when error is not null`() {
        // Given
        val uiState = RecordingUiState(error = "Test error")

        // When
        val hasError = uiState.hasError

        // Then
        assertTrue(hasError)
    }

    @Test
    fun `hasError should be false when error is null`() {
        // Given
        val uiState = RecordingUiState(error = null)

        // When
        val hasError = uiState.hasError

        // Then
        assertFalse(hasError)
    }

    @Test
    fun `needsPermission should be true when permission is not granted`() {
        // Given
        val uiState = RecordingUiState(permissionState = PermissionState.Denied)

        // When
        val needsPermission = uiState.needsPermission

        // Then
        assertTrue(needsPermission)
    }

    @Test
    fun `needsPermission should be false when permission is granted`() {
        // Given
        val uiState = RecordingUiState(permissionState = PermissionState.Granted)

        // When
        val needsPermission = uiState.needsPermission

        // Then
        assertFalse(needsPermission)
    }

    @Test
    fun `showPermissionError should be true when permission is permanently denied`() {
        // Given
        val uiState = RecordingUiState(permissionState = PermissionState.PermanentlyDenied)

        // When
        val showError = uiState.showPermissionError

        // Then
        assertTrue(showError)
    }

    @Test
    fun `showPermissionError should be false when permission is not permanently denied`() {
        // Given
        val uiState = RecordingUiState(permissionState = PermissionState.Granted)

        // When
        val showError = uiState.showPermissionError

        // Then
        assertFalse(showError)
    }

    @Test
    fun `showPermissionError should be false when permission is denied but not permanently`() {
        // Given
        val uiState = RecordingUiState(permissionState = PermissionState.Denied)

        // When
        val showError = uiState.showPermissionError

        // Then
        assertFalse(showError)
    }
}
