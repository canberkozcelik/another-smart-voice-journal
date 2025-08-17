package com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback

import com.example.anothersmartvoicejournal.feature.journal.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import javax.inject.Inject

class StopPlaybackUseCase @Inject constructor(
    private val repository: PlaybackRepository
) {

    suspend fun execute(): PlaybackState {
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
