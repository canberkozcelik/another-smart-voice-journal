package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import javax.inject.Inject

class GetPlaybackDurationUseCase @Inject constructor(
    private val repository: PlaybackRepository
) {
    
    suspend fun execute(audioFilePath: String?): Long {
        // Input validation
        if (audioFilePath.isNullOrBlank()) {
            return -1L // Error indicator
        }
        
        // Get duration from repository (real MediaPlayer implementation)
        return repository.getAudioDuration(audioFilePath)
    }
}
