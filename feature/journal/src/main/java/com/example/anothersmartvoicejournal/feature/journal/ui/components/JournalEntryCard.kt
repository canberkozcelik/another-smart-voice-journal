package com.example.anothersmartvoicejournal.feature.journal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.model.JournalEntryUiModel
import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState

@Composable
fun JournalEntryCard(
    entry: JournalEntryUiModel,
    playbackState: PlaybackState? = null,
    duration: Long = 0L,
    currentPosition: Long = 0L,
    onClick: () -> Unit,
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(entry.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(entry.date, style = MaterialTheme.typography.bodySmall)
            entry.summary?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (entry.hasAudio) {
                Spacer(Modifier.height(16.dp))
                PlaybackControl(
                    playbackState = playbackState ?: PlaybackState.Idle,
                    duration = duration,
                    currentPosition = currentPosition,
                    onPlay = onPlayClick,
                    onPause = onPauseClick,
                    onStop = onStopClick
                )
            }
        }
    }
}

@Preview(name = "Journal Entry Card - With Summary and Audio")
@Composable
private fun JournalEntryCardPreview_WithSummaryAndAudio() {
    MaterialTheme {
        JournalEntryCard(
            entry = JournalEntryUiModel(
                id = "1",
                title = "My Morning Thoughts",
                date = "Aug 01, 2025",
                summary = "• Started the day with meditation\n• Planned the upcoming project timeline",
                hasAudio = true
            ),
            playbackState = PlaybackState.Playing("/test/audio.mp3", 30000L),
            duration = 120000L,
            currentPosition = 30000L,
            onClick = {},
            onPlayClick = {},
            onPauseClick = {},
            onStopClick = {}
        )
    }
}

@Preview(name = "Journal Entry Card - With Summary, No Audio")
@Composable
private fun JournalEntryCardPreview_WithSummaryNoAudio() {
    MaterialTheme {
        JournalEntryCard(
            entry = JournalEntryUiModel(
                id = "2",
                title = "Project Ideas",
                date = "Jul 31, 2025",
                summary = "• New feature ideas for the voice journal app\n• User feedback integration",
                hasAudio = false
            ),
            onClick = {},
            onPlayClick = {},
            onPauseClick = {},
            onStopClick = {}
        )
    }
}

@Preview(name = "Journal Entry Card - No Summary, With Audio")
@Composable
private fun JournalEntryCardPreview_NoSummaryWithAudio() {
    MaterialTheme {
        JournalEntryCard(
            entry = JournalEntryUiModel(
                id = "3",
                title = "Quick Note",
                date = "Jul 30, 2025",
                summary = null,
                hasAudio = true
            ),
            playbackState = PlaybackState.Paused("/test/audio.mp3", 45000L),
            duration = 90000L,
            currentPosition = 45000L,
            onClick = {},
            onPlayClick = {},
            onPauseClick = {},
            onStopClick = {}
        )
    }
}

@Preview(name = "Journal Entry Card - No Summary, No Audio")
@Composable
private fun JournalEntryCardPreview_NoSummaryNoAudio() {
    MaterialTheme {
        JournalEntryCard(
            entry = JournalEntryUiModel(
                id = "4",
                title = "Draft Entry",
                date = "Jul 29, 2025",
                summary = null,
                hasAudio = false
            ),
            onClick = {},
            onPlayClick = {},
            onPauseClick = {},
            onStopClick = {}
        )
    }
}
