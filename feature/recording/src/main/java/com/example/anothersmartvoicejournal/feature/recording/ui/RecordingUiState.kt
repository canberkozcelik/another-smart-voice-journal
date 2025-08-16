package com.example.anothersmartvoicejournal.feature.recording.ui

import java.util.Locale

data class RecordingUiState(
    val isLoading: Boolean = false,
    val isRecording: Boolean = false,
    val duration: Long = 0L,
    val filePath: String? = null,
    val error: String? = null,
    val permissionState: PermissionState = PermissionState.NotRequested
) {
    val formattedDuration: String
        get() {
            // Only show duration when not recording (after completion)
            if (isRecording) return ""

            val seconds = (duration / 1000).toInt()
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
        }

    val canStartRecording: Boolean
        get() = !isLoading && !isRecording && (
            permissionState == PermissionState.Granted ||
                permissionState == PermissionState.NotRequested
            )

    val canStopRecording: Boolean
        get() = !isLoading && isRecording

    val hasError: Boolean
        get() = error != null

    val needsPermission: Boolean
        get() = permissionState != PermissionState.Granted

    val showPermissionError: Boolean
        get() = permissionState == PermissionState.PermanentlyDenied
}

sealed class PermissionState {
    data object NotRequested : PermissionState()
    data object Requesting : PermissionState()
    data object Granted : PermissionState()
    data object Denied : PermissionState()
    data object PermanentlyDenied : PermissionState()
}
