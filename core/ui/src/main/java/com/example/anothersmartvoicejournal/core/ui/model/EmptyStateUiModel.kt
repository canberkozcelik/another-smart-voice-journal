package com.example.anothersmartvoicejournal.core.ui.model

data class EmptyStateUiModel(
    val title: String,
    val message: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    val actionText: String? = null,
    val onActionClick: (() -> Unit)? = null
)
