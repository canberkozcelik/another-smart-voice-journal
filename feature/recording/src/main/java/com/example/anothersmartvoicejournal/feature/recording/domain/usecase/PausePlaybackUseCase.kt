package com.example.anothersmartvoicejournal.feature.recording.domain.usecase

import com.example.anothersmartvoicejournal.feature.recording.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.recording.domain.PlaybackState
import javax.inject.Inject

class PausePlaybackUseCase @Inject constructor(
    private val repository: PlaybackRepository
) {
    
    suspend fun execute(currentState: PlaybackState): PlaybackState {
        return when (currentState) {
            is PlaybackState.Playing -> {
                // Pause playback using repository
                val result = repository.pausePlayback()
                
                if (result.isSuccess) {
                    // Can pause from playing state - preserve position
                    PlaybackState.Paused(currentState.audioFilePath, currentState.currentPosition)
                } else {
                    // Handle pause failure
                    PlaybackState.Error("Failed to pause playback: ${result.exceptionOrNull()?.message}")
                }
            }
            is PlaybackState.Paused -> {
                // Cannot pause if already paused
                PlaybackState.Error("Cannot pause: already paused")
            }
            is PlaybackState.Idle -> {
                // Cannot pause if not playing
                PlaybackState.Error("Cannot pause: not currently playing")
            }
            is PlaybackState.Error -> {
                // Cannot pause from error state
                PlaybackState.Error("Cannot pause: not currently playing")
            }
        }
    }
}
