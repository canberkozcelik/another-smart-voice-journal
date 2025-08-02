package com.example.anothersmartvoicejournal.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anothersmartvoicejournal.core.ui.model.JournalEntryUiModel

@Composable
fun JournalEntryCard(
    entry: JournalEntryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPlayClick: (() -> Unit)? = null
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
            if (entry.hasAudio && onPlayClick != null) {
                Spacer(Modifier.height(8.dp))
                IconButton(onClick = onPlayClick) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play Audio")
                }
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
            onClick = {},
            onPlayClick = {}
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
            onPlayClick = null
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
            onClick = {},
            onPlayClick = {}
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
            onPlayClick = null
        )
    }
}
