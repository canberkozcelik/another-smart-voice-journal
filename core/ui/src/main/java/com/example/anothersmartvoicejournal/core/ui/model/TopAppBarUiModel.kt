package com.example.anothersmartvoicejournal.core.ui.model

data class TopAppBarUiModel(
    val title: String,
    val showBackButton: Boolean = false,
    val onNavigateBack: (() -> Unit)? = null
)
