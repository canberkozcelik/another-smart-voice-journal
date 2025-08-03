package com.example.anothersmartvoicejournal.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.anothersmartvoicejournal.core.ui.model.TopAppBarUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    uiModel: TopAppBarUiModel,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = uiModel.title) },
        navigationIcon = {
            if (uiModel.showBackButton && uiModel.onNavigateBack != null) {
                IconButton(onClick = uiModel.onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        actions = actions,
        modifier = modifier
    )
}

@Preview(name = "Top App Bar - With Back Navigation")
@Composable
private fun TopAppBarPreview_WithBackNavigation() {
    MaterialTheme {
        TopAppBar(
            uiModel = TopAppBarUiModel(
                title = "Journal Entry",
                showBackButton = true,
                onNavigateBack = {}
            )
        )
    }
}

@Preview(name = "Top App Bar - Without Back Navigation")
@Composable
private fun TopAppBarPreview_WithoutBackNavigation() {
    MaterialTheme {
        TopAppBar(
            uiModel = TopAppBarUiModel(
                title = "Voice Journal",
                showBackButton = false
            )
        )
    }
}

@Preview(name = "Top App Bar - With Actions")
@Composable
private fun TopAppBarPreview_WithActions() {
    MaterialTheme {
        TopAppBar(
            uiModel = TopAppBarUiModel(
                title = "Settings",
                showBackButton = true,
                onNavigateBack = {}
            ),
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
            }
        )
    }
}
