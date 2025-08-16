package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.recording.domain.PlaybackState
import javax.inject.Inject

class StopPlaybackUseCase @Inject constructor(
    private val repository: PlaybackRepository
) {
    
    suspend fun execute(currentState: PlaybackState): PlaybackState {
        // Stop playback using repository
        val result = repository.stopPlayback()
        
        return if (result.isSuccess) {
            // Stop always returns to idle state regardless of current state
            PlaybackState.Idle
        } else {
            // Handle stop failure
            PlaybackState.Error("Failed to stop playback: ${result.exceptionOrNull()?.message}")
        }
    }
}
