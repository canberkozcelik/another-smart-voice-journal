package com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback

import com.example.anothersmartvoicejournal.feature.journal.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import javax.inject.Inject

class StartPlaybackUseCase @Inject constructor(
    private val repository: PlaybackRepository
) {

    suspend fun execute(audioFilePath: String, currentState: PlaybackState): PlaybackState {
        // Validate input
        if (audioFilePath.isBlank()) {
            return PlaybackState.Error("Invalid audio file path")
        }

        // Start playback using repository
        val result = repository.startPlayback(audioFilePath)

        return if (result.isSuccess) {
            // Handle state transitions based on current state
            when (currentState) {
                is PlaybackState.Idle -> {
                    PlaybackState.Playing(audioFilePath)
                }
                is PlaybackState.Playing -> {
                    // Stop current and start new
                    PlaybackState.Playing(audioFilePath)
                }
                is PlaybackState.Paused -> {
                    // Resume from paused with current position
                    PlaybackState.Playing(audioFilePath, currentState.currentPosition)
                }
                is PlaybackState.Error -> {
                    // Reset from error state
                    PlaybackState.Playing(audioFilePath)
                }
            }
        } else {
            // Handle playback start failure
            PlaybackState.Error("Failed to start playback: ${result.exceptionOrNull()?.message}")
        }
    }
}
