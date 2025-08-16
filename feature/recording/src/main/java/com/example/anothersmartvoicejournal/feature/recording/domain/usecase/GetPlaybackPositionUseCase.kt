package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import javax.inject.Inject

class GetPlaybackPositionUseCase @Inject constructor(
    private val repository: PlaybackRepository
) {
    
    suspend fun execute(): Long {
        // Get current position from repository (real MediaPlayer implementation)
        return repository.getCurrentPosition()
    }
}
