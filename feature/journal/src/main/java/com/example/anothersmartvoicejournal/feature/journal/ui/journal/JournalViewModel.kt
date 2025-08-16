package com.example.anothersmartvoicejournal.feature.journal.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anothersmartvoicejournal.core.domain.usecase.DeleteJournalEntryUseCase
import com.example.anothersmartvoicejournal.core.domain.usecase.GetJournalEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val getJournalEntriesUseCase: GetJournalEntriesUseCase,
    private val deleteJournalEntryUseCase: DeleteJournalEntryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    init {
        loadJournalEntries()
    }

    fun loadJournalEntries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                getJournalEntriesUseCase().collect { entries ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            journalEntries = entries,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load journal entries"
                    )
                }
            }
        }
    }

    fun deleteEntry(id: String) {
        viewModelScope.launch {
            try {
                val result = deleteJournalEntryUseCase(id)
                if (result.isSuccess) {
                    // Reload entries after successful deletion
                    loadJournalEntries()
                } else {
                    _uiState.update {
                        it.copy(
                            error = "Failed to delete entry"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to delete entry"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
