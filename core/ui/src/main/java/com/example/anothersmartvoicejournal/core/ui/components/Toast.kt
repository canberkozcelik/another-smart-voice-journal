package com.example.anothersmartvoicejournal.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.model.ToastType
import com.example.anothersmartvoicejournal.core.ui.model.ToastUiModel
import kotlinx.coroutines.delay

@Composable
fun Toast(
    uiModel: ToastUiModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(uiModel) {
        delay(uiModel.duration)
        isVisible = false
        onDismiss()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(300),
            initialOffsetY = { -it }
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            animationSpec = tween(300),
            targetOffsetY = { -it }
        ) + fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getToastBackgroundColor(uiModel.type))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = getToastIcon(uiModel.type),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = getToastIconColor(uiModel.type)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = uiModel.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = getToastTextColor(uiModel.type),
                        textAlign = TextAlign.Start
                    )

                    uiModel.actionText?.let { actionText ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                uiModel.onActionClick?.invoke()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = getToastTextColor(uiModel.type)
                            ),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = actionText,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getToastIcon(type: ToastType) = when (type) {
    ToastType.SUCCESS -> Icons.Default.CheckCircle
    ToastType.ERROR -> Icons.Default.Error
    ToastType.WARNING -> Icons.Default.Warning
    ToastType.INFO -> Icons.Default.Info
}

@Composable
private fun getToastBackgroundColor(type: ToastType) = when (type) {
    ToastType.SUCCESS -> MaterialTheme.colorScheme.primaryContainer
    ToastType.ERROR -> MaterialTheme.colorScheme.errorContainer
    ToastType.WARNING -> MaterialTheme.colorScheme.tertiaryContainer
    ToastType.INFO -> MaterialTheme.colorScheme.secondaryContainer
}

@Composable
private fun getToastIconColor(type: ToastType) = when (type) {
    ToastType.SUCCESS -> MaterialTheme.colorScheme.onPrimaryContainer
    ToastType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
    ToastType.WARNING -> MaterialTheme.colorScheme.onTertiaryContainer
    ToastType.INFO -> MaterialTheme.colorScheme.onSecondaryContainer
}

@Composable
private fun getToastTextColor(type: ToastType) = when (type) {
    ToastType.SUCCESS -> MaterialTheme.colorScheme.onPrimaryContainer
    ToastType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
    ToastType.WARNING -> MaterialTheme.colorScheme.onTertiaryContainer
    ToastType.INFO -> MaterialTheme.colorScheme.onSecondaryContainer
}

@Preview(name = "Toast - Success")
@Composable
private fun ToastPreview_Success() {
    MaterialTheme {
        Toast(
            uiModel = ToastUiModel(
                message = "Journal entry saved successfully!",
                type = ToastType.SUCCESS
            ),
            onDismiss = {}
        )
    }
}

@Preview(name = "Toast - Error with Action")
@Composable
private fun ToastPreview_ErrorWithAction() {
    MaterialTheme {
        Toast(
            uiModel = ToastUiModel(
                message = "Failed to save journal entry. Please try again.",
                type = ToastType.ERROR,
                actionText = "Retry",
                onActionClick = {}
            ),
            onDismiss = {}
        )
    }
}

@Preview(name = "Toast - Info")
@Composable
private fun ToastPreview_Info() {
    MaterialTheme {
        Toast(
            uiModel = ToastUiModel(
                message = "Recording in progress...",
                type = ToastType.INFO
            ),
            onDismiss = {}
        )
    }
}