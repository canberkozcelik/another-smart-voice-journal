package com.example.anothersmartvoicejournal.core.ui.model

data class LoadingIndicatorUiModel(
    val state: LoadingState = LoadingState.LOADING,
    val type: LoadingType = LoadingType.CIRCULAR,
    val message: String? = null
)

enum class LoadingType {
    CIRCULAR,
    LINEAR
}

enum class LoadingState {
    LOADING,
    SUCCESS,
    ERROR
}
