package com.example.anothersmartvoicejournal.core.ui.components

import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.model.RecordButtonState
import com.example.anothersmartvoicejournal.core.ui.model.RecordButtonUiModel

@Composable
fun RecordButton(
    uiModel: RecordButtonUiModel,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "recordButton")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (uiModel.state == RecordButtonState.RECORDING) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    FloatingActionButton(
        onClick = {
            if (uiModel.enabled && uiModel.state != RecordButtonState.PROCESSING) {
                uiModel.onClick()
            }
        },
        modifier = modifier
            .size(if (uiModel.state == RecordButtonState.RECORDING) 72.dp else 56.dp)
            .graphicsLayer {
                scaleX = if (uiModel.state == RecordButtonState.RECORDING) scale else 1f
                scaleY = if (uiModel.state == RecordButtonState.RECORDING) scale else 1f
            },
        containerColor = when (uiModel.state) {
            RecordButtonState.IDLE -> MaterialTheme.colorScheme.primary
            RecordButtonState.RECORDING -> MaterialTheme.colorScheme.inversePrimary
            RecordButtonState.PROCESSING -> MaterialTheme.colorScheme.surfaceVariant
            RecordButtonState.DISABLED -> MaterialTheme.colorScheme.error
        }
    ) {
        when (uiModel.state) {
            RecordButtonState.IDLE -> {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Start Recording",
                    modifier = Modifier.size(24.dp)
                )
            }
            RecordButtonState.RECORDING -> {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop Recording",
                    modifier = Modifier.size(24.dp)
                )
            }
            RecordButtonState.PROCESSING -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    strokeWidth = 2.dp
                )
            }
            RecordButtonState.DISABLED -> {
                Icon(
                    imageVector = Icons.Default.MicOff,
                    contentDescription = "Recording Disabled",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(name = "Record Button - Idle")
@Composable
private fun RecordButtonPreview_Idle() {
    MaterialTheme {
        RecordButton(
            uiModel = RecordButtonUiModel(
                state = RecordButtonState.IDLE,
                enabled = true,
                onClick = {}
            )
        )
    }
}

@Preview(name = "Record Button - Recording")
@Composable
private fun RecordButtonPreview_Recording() {
    MaterialTheme {
        RecordButton(
            uiModel = RecordButtonUiModel(
                state = RecordButtonState.RECORDING,
                enabled = true,
                onClick = {}
            )
        )
    }
}

@Preview(name = "Record Button - Processing")
@Composable
private fun RecordButtonPreview_Processing() {
    MaterialTheme {
        RecordButton(
            uiModel = RecordButtonUiModel(
                state = RecordButtonState.PROCESSING,
                enabled = true,
                onClick = {}
            )
        )
    }
}

@Preview(name = "Record Button - Disabled")
@Composable
private fun RecordButtonPreview_Disabled() {
    MaterialTheme {
        RecordButton(
            uiModel = RecordButtonUiModel(
                state = RecordButtonState.DISABLED,
                enabled = false,
                onClick = {}
            )
        )
    }
}
