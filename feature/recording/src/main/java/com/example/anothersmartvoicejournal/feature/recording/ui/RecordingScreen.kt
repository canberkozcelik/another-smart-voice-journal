package com.example.anothersmartvoicejournal.feature.recording.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.example.anothersmartvoicejournal.core.ui.components.LoadingIndicator
import com.example.anothersmartvoicejournal.core.ui.components.RecordButton
import com.example.anothersmartvoicejournal.core.ui.components.Toast
import com.example.anothersmartvoicejournal.core.ui.components.TopAppBar
import com.example.anothersmartvoicejournal.core.ui.model.LoadingIndicatorUiModel
import com.example.anothersmartvoicejournal.core.ui.model.LoadingState
import com.example.anothersmartvoicejournal.core.ui.model.LoadingType
import com.example.anothersmartvoicejournal.core.ui.model.RecordButtonState
import com.example.anothersmartvoicejournal.core.ui.model.RecordButtonUiModel
import com.example.anothersmartvoicejournal.core.ui.model.ToastType
import com.example.anothersmartvoicejournal.core.ui.model.ToastUiModel
import com.example.anothersmartvoicejournal.core.ui.model.TopAppBarUiModel
import kotlinx.coroutines.delay

@Composable
fun RecordingScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecordingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Permission launcher with stable callback
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val permissionState = if (isGranted) {
            PermissionState.Granted
        } else {
            // Check if user permanently denied by checking if we should show rationale
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                val activity = context as? ComponentActivity
                if (activity?.shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) == true) {
                    PermissionState.Denied
                } else {
                    PermissionState.PermanentlyDenied
                }
            } else {
                PermissionState.Denied
            }
        }
        viewModel.updatePermissionState(permissionState)
    }

    // Remember stable callbacks to prevent recomposition
    val onStartRecording = remember(viewModel) {
        {
            when (uiState.permissionState) {
                PermissionState.Granted -> viewModel.startRecording()
                PermissionState.NotRequested, PermissionState.Denied -> {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                PermissionState.PermanentlyDenied -> {
                    // Show settings dialog or error - handled by UI below
                }
                PermissionState.Requesting -> {
                    // Do nothing, already requesting
                }
            }
        }
    }

    // Remember stable callbacks for other actions
    val onStopRecording = remember(viewModel) { { viewModel.stopRecording() } }
    val onClearError = remember(viewModel) { { viewModel.clearError() } }

    // Check initial permission state
    LaunchedEffect(Unit) {
        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        val permissionState = if (permission == PackageManager.PERMISSION_GRANTED) {
            PermissionState.Granted
        } else {
            // Check if we should show rationale to determine if permanently denied
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                val activity = context as? ComponentActivity
                if (activity?.shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) == true) {
                    PermissionState.Denied
                } else {
                    PermissionState.NotRequested
                }
            } else {
                PermissionState.NotRequested
            }
        }
        viewModel.updatePermissionState(permissionState)
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            delay(4000) // Show error for 4 seconds
            viewModel.clearError()
        }
    }

    // Remember stable UI models
    val permissionErrorToastUiModel = remember {
        ToastUiModel(
            message = "Microphone permission is required for recording. Please enable it in Settings.",
            type = ToastType.ERROR,
            duration = 6000L
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            RecordingTopAppBar(onNavigateBack = onNavigateBack)

            RecordingContent(
                uiState = uiState,
                onStartRecording = onStartRecording,
                onStopRecording = onStopRecording
            )
        }

        RecordingErrorToast(
            error = uiState.error,
            onDismiss = onClearError
        )

        // Show permission error if permanently denied
        if (uiState.showPermissionError) {
            Toast(
                uiModel = permissionErrorToastUiModel,
                onDismiss = {
                    // Don't update state here, just dismiss the toast
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun RecordingTopAppBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        uiModel = TopAppBarUiModel(
            title = "Voice Recording",
            showBackButton = true,
            onNavigateBack = onNavigateBack
        )
    )
}

@Composable
private fun RecordingContent(
    uiState: RecordingUiState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RecordingStatusCard(uiState = uiState)

            Spacer(modifier = Modifier.height(32.dp))

            RecordingControls(
                uiState = uiState,
                onStartRecording = onStartRecording,
                onStopRecording = onStopRecording
            )
        }
    }
}

@Composable
private fun RecordingStatusCard(uiState: RecordingUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RecordingStatusIcon(uiState = uiState)

            Spacer(modifier = Modifier.height(16.dp))

            RecordingStatusText(uiState = uiState)

            RecordingDurationText(uiState = uiState)
        }
    }
}

@Composable
private fun RecordingStatusIcon(uiState: RecordingUiState) {
    if (uiState.isLoading) {
        LoadingIndicator(
            uiModel = LoadingIndicatorUiModel(
                type = LoadingType.CIRCULAR,
                state = LoadingState.LOADING
            )
        )
    } else {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Recording",
            modifier = Modifier.size(48.dp),
            tint = if (uiState.isRecording) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun RecordingStatusText(uiState: RecordingUiState) {
    Text(
        text = when {
            uiState.isLoading -> "Initializing..."
            uiState.isRecording -> "Recording"
            else -> "Ready to Record"
        },
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun RecordingDurationText(uiState: RecordingUiState) {
    // Only show duration when NOT recording (i.e., after recording is complete)
    if (!uiState.isRecording && uiState.duration > 0) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = uiState.formattedDuration,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RecordingControls(
    uiState: RecordingUiState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MainRecordButton(
            uiState = uiState,
            onStartRecording = onStartRecording,
            onStopRecording = onStopRecording
        )
    }
}

@Composable
private fun MainRecordButton(
    uiState: RecordingUiState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    val buttonState = remember(uiState.isRecording, uiState.isLoading, uiState.permissionState, uiState.error) {
        when {
            uiState.isLoading -> RecordButtonState.PROCESSING
            uiState.isRecording -> RecordButtonState.RECORDING
            uiState.permissionState == PermissionState.PermanentlyDenied -> RecordButtonState.DISABLED
            uiState.error != null -> RecordButtonState.DISABLED // Show disabled when there's an error
            else -> RecordButtonState.IDLE
        }
    }

    val isEnabled = remember(uiState.isRecording, uiState.isLoading, uiState.permissionState, uiState.error) {
        // Allow clicks if we can start/stop recording OR if we need to request permission
        // Only disable when permanently denied or when there's an error
        !uiState.isLoading &&
            uiState.permissionState != PermissionState.PermanentlyDenied &&
            uiState.error == null // Disable when there's an error
    }

    val onClick = remember(onStartRecording, onStopRecording, uiState.isRecording, uiState.permissionState) {
        {
            when {
                !uiState.isRecording && (uiState.permissionState == PermissionState.Granted || uiState.permissionState == PermissionState.NotRequested || uiState.permissionState == PermissionState.Denied) -> {
                    onStartRecording()
                }
                uiState.isRecording -> {
                    onStopRecording()
                }
                uiState.permissionState == PermissionState.PermanentlyDenied -> {
                    // Handle permanently denied permission
                }
                else -> {
                    // No action needed
                }
            }
        }
    }

    val uiModel = remember(buttonState, isEnabled, onClick) {
        RecordButtonUiModel(
            state = buttonState,
            enabled = isEnabled,
            onClick = onClick
        )
    }

    RecordButton(uiModel = uiModel)
}

@Composable
private fun RecordingErrorToast(
    error: String?,
    onDismiss: () -> Unit
) {
    error?.let { errorMessage ->
        val toastUiModel = remember(errorMessage) {
            ToastUiModel(
                message = errorMessage,
                type = ToastType.ERROR,
                duration = 4000L
            )
        }

        Toast(
            uiModel = toastUiModel,
            onDismiss = onDismiss,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecordingScreenPreview() {
    MaterialTheme {
        RecordingScreen(
            onNavigateBack = {}
        )
    }
}
