package com.example.anothersmartvoicejournal.core.ui.model

data class ToastUiModel(
    val message: String,
    val type: ToastType = ToastType.INFO,
    val duration: Long = 3000L,
    val actionText: String? = null,
    val onActionClick: (() -> Unit)? = null
)

enum class ToastType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}