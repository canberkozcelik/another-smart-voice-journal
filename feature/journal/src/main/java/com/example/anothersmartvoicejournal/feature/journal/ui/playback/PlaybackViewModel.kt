package com.example.anothersmartvoicejournal.feature.journal.ui.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.GetPlaybackDurationUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.GetPlaybackPositionUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.PausePlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.StartPlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.StopPlaybackUseCase
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val startPlaybackUseCase: StartPlaybackUseCase,
    private val pausePlaybackUseCase: PausePlaybackUseCase,
    private val stopPlaybackUseCase: StopPlaybackUseCase,
    private val getPlaybackDurationUseCase: GetPlaybackDurationUseCase,
    private val getPlaybackPositionUseCase: GetPlaybackPositionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaybackUiState())
    val uiState: StateFlow<PlaybackUiState> = _uiState.asStateFlow()

    fun startPlayback(audioFilePath: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newState = startPlaybackUseCase.execute(audioFilePath, _uiState.value.playbackState)
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    playbackState = newState
                )
            }
        }
    }

    fun pausePlayback() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newState = pausePlaybackUseCase.execute(_uiState.value.playbackState)
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    playbackState = newState
                )
            }
        }
    }

    fun stopPlayback() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val newState = stopPlaybackUseCase.execute()
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    playbackState = newState
                )
            }
        }
    }

    fun getDuration(audioFilePath: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val duration = getPlaybackDurationUseCase.execute(audioFilePath)
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    duration = duration
                )
            }
        }
    }

    fun getCurrentPosition() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val position = getPlaybackPositionUseCase.execute()
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    currentPosition = position
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(playbackState = PlaybackState.Idle) }
    }

    fun showError(errorMessage: String) {
        val errorState = PlaybackState.Error(errorMessage, _uiState.value.playbackState)
        _uiState.update { it.copy(playbackState = errorState) }
    }
}
