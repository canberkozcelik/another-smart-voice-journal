package com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state

sealed class PlaybackState {
    data object Idle : PlaybackState()

    data class Playing(
        val audioFilePath: String,
        val currentPosition: Long = 0L
    ) : PlaybackState()

    data class Paused(
        val audioFilePath: String,
        val currentPosition: Long = 0L
    ) : PlaybackState()

    data class Error(
        val message: String,
        val previousState: PlaybackState? = null
    ) : PlaybackState()
}
