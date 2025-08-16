package com.example.anothersmartvoicejournal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.anothersmartvoicejournal.feature.journal.ui.journal.JournalScreen
import com.example.anothersmartvoicejournal.feature.recording.ui.RecordingScreen

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Recording : Screen("recording")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            JournalScreen(
                onNavigateToRecording = {
                    navController.navigate(Screen.Recording.route)
                }
            )
        }

        composable(Screen.Recording.route) {
            RecordingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
