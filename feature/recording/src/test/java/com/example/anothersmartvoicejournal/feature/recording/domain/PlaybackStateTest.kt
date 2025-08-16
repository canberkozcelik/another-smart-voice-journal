package com.example.anothersmartvoicejournal.feature.recording.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaybackStateTest {

    @Test
    fun `PlaybackState Idle should have correct properties`() {
        // Given
        val state = PlaybackState.Idle

        // Then
        assertTrue(state is PlaybackState.Idle)
    }

    @Test
    fun `PlaybackState Playing should have correct properties`() {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 5000L
        val state = PlaybackState.Playing(audioFilePath, currentPosition)

        // Then
        assertTrue(state is PlaybackState.Playing)
        assertEquals(audioFilePath, state.audioFilePath)
        assertEquals(currentPosition, state.currentPosition)
    }

    @Test
    fun `PlaybackState Paused should have correct properties`() {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 10000L
        val state = PlaybackState.Paused(audioFilePath, currentPosition)

        // Then
        assertTrue(state is PlaybackState.Paused)
        assertEquals(audioFilePath, state.audioFilePath)
        assertEquals(currentPosition, state.currentPosition)
    }

    @Test
    fun `PlaybackState Error should have correct properties`() {
        // Given
        val errorMessage = "File not found"
        val previousState = PlaybackState.Playing("/test/audio.mp3")
        val state = PlaybackState.Error(errorMessage, previousState)

        // Then
        assertTrue(state is PlaybackState.Error)
        assertEquals(errorMessage, state.message)
        assertEquals(previousState, state.previousState)
    }

    @Test
    fun `PlaybackState Error should handle null previous state`() {
        // Given
        val errorMessage = "File not found"
        val state = PlaybackState.Error(errorMessage)

        // Then
        assertTrue(state is PlaybackState.Error)
        assertEquals(errorMessage, state.message)
        assertEquals(null, state.previousState)
    }
}
