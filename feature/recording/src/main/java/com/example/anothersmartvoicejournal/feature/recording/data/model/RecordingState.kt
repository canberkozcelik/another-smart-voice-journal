package com.example.anothersmartvoicejournal.feature.recording.data.model

data class RecordingState(
    val isRecording: Boolean = false,
    val duration: Long = 0L,
    val filePath: String? = null,
    val error: String? = null
)
