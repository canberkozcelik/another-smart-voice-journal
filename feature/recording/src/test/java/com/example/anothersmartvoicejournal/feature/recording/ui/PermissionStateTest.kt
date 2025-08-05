package com.example.anothersmartvoicejournal.feature.recording.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionStateTest {

    @Test
    fun `PermissionState sealed class should have all expected states`() {
        // Given & When
        val notRequested = PermissionState.NotRequested
        val requesting = PermissionState.Requesting
        val granted = PermissionState.Granted
        val denied = PermissionState.Denied
        val permanentlyDenied = PermissionState.PermanentlyDenied

        // Then
        assertEquals(PermissionState.NotRequested, notRequested)
        assertEquals(PermissionState.Requesting, requesting)
        assertEquals(PermissionState.Granted, granted)
        assertEquals(PermissionState.Denied, denied)
        assertEquals(PermissionState.PermanentlyDenied, permanentlyDenied)
    }

    @Test
    fun `PermissionState should be comparable`() {
        // Given
        val state1 = PermissionState.Granted
        val state2 = PermissionState.Granted
        val state3 = PermissionState.Denied

        // When & Then
        assertEquals(state1, state2)
        assertFalse(state1.equals(state3))
    }

    @Test
    fun `PermissionState should work in when expressions`() {
        // Given
        val granted = PermissionState.Granted
        val denied = PermissionState.Denied

        // When & Then
        val grantedResult = when (granted) {
            PermissionState.Granted -> "GRANTED"
            else -> ""
        }
        assertEquals("GRANTED", grantedResult)

        val deniedResult = when (denied) {
            PermissionState.Denied -> "DENIED"
            else -> ""
        }
        assertEquals("DENIED", deniedResult)
    }

    @Test
    fun `PermissionState should work with RecordingUiState`() {
        // Given
        val uiState = RecordingUiState(permissionState = PermissionState.Granted)

        // When & Then
        assertEquals(PermissionState.Granted, uiState.permissionState)
        assertFalse(uiState.needsPermission)
        assertTrue(uiState.canStartRecording)
    }
}
