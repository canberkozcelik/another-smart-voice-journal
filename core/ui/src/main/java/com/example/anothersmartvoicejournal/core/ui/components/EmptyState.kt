package com.example.anothersmartvoicejournal.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.model.EmptyStateUiModel

@Composable
fun EmptyState(
    uiModel: EmptyStateUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        uiModel.icon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = uiModel.title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = uiModel.message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        uiModel.actionText?.let { actionText ->
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { uiModel.onActionClick?.invoke() }
            ) {
                Text(text = actionText)
            }
        }
    }
}

@Preview(name = "Empty State - With Icon and Action")
@Composable
private fun EmptyStatePreview_WithIconAndAction() {
    MaterialTheme {
        EmptyState(
            uiModel = EmptyStateUiModel(
                title = "No Journal Entries",
                message = "Start your voice journaling journey by recording your first entry.",
                icon = Icons.Default.Mic,
                actionText = "Start Recording",
                onActionClick = {}
            )
        )
    }
}

@Preview(name = "Empty State - Without Icon")
@Composable
private fun EmptyStatePreview_WithoutIcon() {
    MaterialTheme {
        EmptyState(
            uiModel = EmptyStateUiModel(
                title = "No Results Found",
                message = "Try adjusting your search criteria or create a new entry."
            )
        )
    }
}

@Preview(name = "Empty State - With Action Only")
@Composable
private fun EmptyStatePreview_WithActionOnly() {
    MaterialTheme {
        EmptyState(
            uiModel = EmptyStateUiModel(
                title = "Connection Error",
                message = "Unable to load your journal entries. Please check your internet connection.",
                actionText = "Retry",
                onActionClick = {}
            )
        )
    }
}