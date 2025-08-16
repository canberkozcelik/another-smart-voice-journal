package com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback

import com.example.anothersmartvoicejournal.feature.journal.data.repository.PlaybackRepository
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
