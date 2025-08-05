package com.example.anothersmartvoicejournal.feature.recording.data.repository

import com.example.anothersmartvoicejournal.feature.recording.data.model.RecordingState
import kotlinx.coroutines.flow.Flow

interface RecordingRepository {
    fun startRecording(): Flow<RecordingState>
    fun stopRecording(): Flow<RecordingState>
    fun getCurrentRecordingState(): Flow<RecordingState>
    fun getRecordingDuration(): Flow<Long>
    fun isRecording(): Boolean
    fun getRecordingFilePath(): String?
}
