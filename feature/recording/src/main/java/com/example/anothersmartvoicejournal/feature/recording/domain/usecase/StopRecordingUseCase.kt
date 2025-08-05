package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.model.RecordingState
import com.example.anothersmartvoicejournal.feature.recording.data.repository.RecordingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StopRecordingUseCase @Inject constructor(
    private val recordingRepository: RecordingRepository
) {
    operator fun invoke(): Flow<RecordingState> {
        return recordingRepository.stopRecording()
    }
}
