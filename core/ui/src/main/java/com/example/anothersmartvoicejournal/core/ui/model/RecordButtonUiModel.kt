package com.example.anothersmartvoicejournal.core.ui.model

data class RecordButtonUiModel(
    val state: RecordButtonState,
    val enabled: Boolean = true,
    val onClick: () -> Unit
)

enum class RecordButtonState {
    IDLE,
    RECORDING,
    PROCESSING
}
