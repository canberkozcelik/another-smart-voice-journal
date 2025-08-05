package com.example.anothersmartvoicejournal.feature.recording.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.GetRecordingDurationUseCase
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.GetRecordingStateUseCase
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.StartRecordingUseCase
import com.example.anothersmartvoicejournal.feature.recording.domain.usecase.StopRecordingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val startRecordingUseCase: StartRecordingUseCase,
    private val stopRecordingUseCase: StopRecordingUseCase,
    private val getRecordingStateUseCase: GetRecordingStateUseCase,
    private val getRecordingDurationUseCase: GetRecordingDurationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState: StateFlow<RecordingUiState> = _uiState.asStateFlow()

    fun startRecording() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            startRecordingUseCase().collect { recordingState ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isRecording = recordingState.isRecording,
                        duration = recordingState.duration,
                        filePath = recordingState.filePath,
                        error = recordingState.error
                    )
                }
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            stopRecordingUseCase().collect { recordingState ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isRecording = recordingState.isRecording,
                        duration = recordingState.duration,
                        filePath = recordingState.filePath,
                        error = recordingState.error
                    )
                }
            }
        }
    }



    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun showError(error: String?) {
        _uiState.update { it.copy(error = error) }
    }

    fun updatePermissionState(permissionState: PermissionState) {
        _uiState.update { it.copy(permissionState = permissionState) }
    }

    private fun observeRecordingState() {
        viewModelScope.launch {
            getRecordingStateUseCase().collect { recordingState ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isRecording = recordingState.isRecording,
                        filePath = recordingState.filePath,
                        error = recordingState.error
                    )
                }
            }
        }
    }

    private fun observeRecordingDuration() {
        viewModelScope.launch {
            getRecordingDurationUseCase().collect { duration ->
                _uiState.update { it.copy(duration = duration) }
            }
        }
    }
}
