package com.example.anothersmartvoicejournal.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.model.LoadingIndicatorUiModel
import com.example.anothersmartvoicejournal.core.ui.model.LoadingState
import com.example.anothersmartvoicejournal.core.ui.model.LoadingType

@Composable
fun LoadingIndicator(
    uiModel: LoadingIndicatorUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiModel.state) {
            LoadingState.LOADING -> {
                when (uiModel.type) {
                    LoadingType.CIRCULAR -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    LoadingType.LINEAR -> {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            LoadingState.SUCCESS -> {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            LoadingState.ERROR -> {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        uiModel.message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = when (uiModel.state) {
                    LoadingState.LOADING -> MaterialTheme.colorScheme.onSurface
                    LoadingState.SUCCESS -> MaterialTheme.colorScheme.primary
                    LoadingState.ERROR -> MaterialTheme.colorScheme.error
                }
            )
        }
    }
}

@Preview(name = "Loading Indicator - Circular")
@Composable
private fun LoadingIndicatorPreview_Circular() {
    MaterialTheme {
        LoadingIndicator(
            uiModel = LoadingIndicatorUiModel(
                state = LoadingState.LOADING,
                type = LoadingType.CIRCULAR,
                message = "Processing your recording..."
            )
        )
    }
}

@Preview(name = "Loading Indicator - Linear")
@Composable
private fun LoadingIndicatorPreview_Linear() {
    MaterialTheme {
        LoadingIndicator(
            uiModel = LoadingIndicatorUiModel(
                state = LoadingState.LOADING,
                type = LoadingType.LINEAR,
                message = "Transcribing audio..."
            )
        )
    }
}

@Preview(name = "Loading Indicator - Success")
@Composable
private fun LoadingIndicatorPreview_Success() {
    MaterialTheme {
        LoadingIndicator(
            uiModel = LoadingIndicatorUiModel(
                state = LoadingState.SUCCESS,
                message = "Recording saved successfully!"
            )
        )
    }
}

@Preview(name = "Loading Indicator - Error")
@Composable
private fun LoadingIndicatorPreview_Error() {
    MaterialTheme {
        LoadingIndicator(
            uiModel = LoadingIndicatorUiModel(
                state = LoadingState.ERROR,
                message = "Failed to process recording"
            )
        )
    }
}

@Preview(name = "Loading Indicator - Simple")
@Composable
private fun LoadingIndicatorPreview_Simple() {
    MaterialTheme {
        LoadingIndicator(
            uiModel = LoadingIndicatorUiModel(
                message = "Processing..."
            )
        )
    }
}
