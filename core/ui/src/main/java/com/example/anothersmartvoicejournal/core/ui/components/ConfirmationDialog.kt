package com.example.anothersmartvoicejournal.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.model.ConfirmationDialogUiModel

@Composable
fun ConfirmationDialog(
    uiModel: ConfirmationDialogUiModel,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = uiModel.onDismiss,
        title = {
            Text(
                text = uiModel.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = uiModel.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    uiModel.onConfirm()
                    uiModel.onDismiss()
                },
                colors = if (uiModel.isDestructive) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(text = uiModel.confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = uiModel.onDismiss
            ) {
                Text(text = uiModel.dismissText)
            }
        },
        modifier = modifier
    )
}

@Preview(name = "Confirmation Dialog - Standard")
@Composable
private fun ConfirmationDialogPreview_Standard() {
    MaterialTheme {
        ConfirmationDialog(
            uiModel = ConfirmationDialogUiModel(
                title = "Save Changes",
                message = "Do you want to save your changes before leaving?",
                confirmText = "Save",
                dismissText = "Don't Save",
                onConfirm = {},
                onDismiss = {}
            )
        )
    }
}

@Preview(name = "Confirmation Dialog - Destructive")
@Composable
private fun ConfirmationDialogPreview_Destructive() {
    MaterialTheme {
        ConfirmationDialog(
            uiModel = ConfirmationDialogUiModel(
                title = "Delete Journal Entry",
                message = "Are you sure you want to delete this journal entry? This action cannot be undone.",
                confirmText = "Delete",
                dismissText = "Cancel",
                isDestructive = true,
                onConfirm = {},
                onDismiss = {}
            )
        )
    }
}

@Preview(name = "Confirmation Dialog - Custom Actions")
@Composable
private fun ConfirmationDialogPreview_CustomActions() {
    MaterialTheme {
        ConfirmationDialog(
            uiModel = ConfirmationDialogUiModel(
                title = "Discard Recording",
                message = "Your current recording will be lost. Do you want to continue?",
                confirmText = "Discard",
                dismissText = "Keep Recording",
                isDestructive = true,
                onConfirm = {},
                onDismiss = {}
            )
        )
    }
}