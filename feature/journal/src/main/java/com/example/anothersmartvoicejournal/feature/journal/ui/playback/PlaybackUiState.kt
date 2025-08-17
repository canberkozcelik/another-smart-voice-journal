package com.example.anothersmartvoicejournal.feature.journal.ui.playback

import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState

data class PlaybackUiState(
    val isLoading: Boolean = false,
    val playbackState: PlaybackState = PlaybackState.Idle,
    val error: String? = null,
    val duration: Long = 0L,
    val currentPosition: Long = 0L
)
