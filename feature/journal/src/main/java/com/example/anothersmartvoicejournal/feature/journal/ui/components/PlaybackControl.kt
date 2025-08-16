package com.example.anothersmartvoicejournal.feature.journal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import java.util.Locale

@Composable
fun PlaybackControl(
    playbackState: PlaybackState,
    duration: Long, // in milliseconds
    currentPosition: Long, // in milliseconds
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress Bar
        LinearProgressIndicator(
            progress = { if (duration > 0) (currentPosition.toFloat() / duration.toFloat()) else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Time Display
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPosition),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatTime(duration),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Control Buttons
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play/Pause Button
            IconButton(
                onClick = when (playbackState) {
                    is PlaybackState.Playing -> onPause
                    else -> onPlay
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = when (playbackState) {
                        is PlaybackState.Playing -> Icons.Filled.Pause
                        else -> Icons.Filled.PlayArrow
                    },
                    contentDescription = when (playbackState) {
                        is PlaybackState.Playing -> "Pause"
                        else -> "Play"
                    },
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Stop Button
            IconButton(
                onClick = onStop,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = "Stop",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Playback State Text
        when (playbackState) {
            is PlaybackState.Error -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = playbackState.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            is PlaybackState.Playing -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Playing",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            is PlaybackState.Paused -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Paused",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is PlaybackState.Idle -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ready to play",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun formatTime(timeInMillis: Long): String {
    if (timeInMillis <= 0) return "0:00"

    val totalSeconds = timeInMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return if (minutes >= 60) {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, remainingMinutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
}

@Preview(name = "Playback Control - Playing")
@Composable
private fun PlaybackControlPreview_Playing() {
    MaterialTheme {
        PlaybackControl(
            playbackState = PlaybackState.Playing("/test/audio.mp3", 30000L),
            duration = 120000L, // 2 minutes
            currentPosition = 30000L, // 30 seconds
            onPlay = {},
            onPause = {},
            onStop = {}
        )
    }
}

@Preview(name = "Playback Control - Paused")
@Composable
private fun PlaybackControlPreview_Paused() {
    MaterialTheme {
        PlaybackControl(
            playbackState = PlaybackState.Paused("/test/audio.mp3", 45000L),
            duration = 120000L, // 2 minutes
            currentPosition = 45000L, // 45 seconds
            onPlay = {},
            onPause = {},
            onStop = {}
        )
    }
}

@Preview(name = "Playback Control - Idle")
@Composable
private fun PlaybackControlPreview_Idle() {
    MaterialTheme {
        PlaybackControl(
            playbackState = PlaybackState.Idle,
            duration = 90000L, // 1.5 minutes
            currentPosition = 0L,
            onPlay = {},
            onPause = {},
            onStop = {}
        )
    }
}

@Preview(name = "Playback Control - Error")
@Composable
private fun PlaybackControlPreview_Error() {
    MaterialTheme {
        PlaybackControl(
            playbackState = PlaybackState.Error("Failed to load audio", PlaybackState.Idle),
            duration = 0L,
            currentPosition = 0L,
            onPlay = {},
            onPause = {},
            onStop = {}
        )
    }
}
