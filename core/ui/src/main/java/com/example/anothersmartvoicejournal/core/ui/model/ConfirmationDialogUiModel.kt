package com.example.anothersmartvoicejournal.core.ui.model

data class ConfirmationDialogUiModel(
    val title: String,
    val message: String,
    val confirmText: String = "Confirm",
    val dismissText: String = "Cancel",
    val isDestructive: Boolean = false,
    val onConfirm: () -> Unit,
    val onDismiss: () -> Unit
)