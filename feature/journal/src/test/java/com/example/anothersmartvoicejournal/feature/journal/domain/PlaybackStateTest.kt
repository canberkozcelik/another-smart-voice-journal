package com.example.anothersmartvoicejournal.feature.journal.domain

import com.example.anothersmartvoicejournal.feature.journal.domain.usecase.playback.state.PlaybackState
import kotlin.test.assertEquals
import org.junit.Assert
import org.junit.Test

class PlaybackStateTest {

    @Test
    fun `PlaybackState Playing should have correct properties`() {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 5000L
        val state = PlaybackState.Playing(audioFilePath, currentPosition)

        // Then
        Assert.assertEquals(audioFilePath, state.audioFilePath)
        Assert.assertEquals(currentPosition, state.currentPosition)
    }

    @Test
    fun `PlaybackState Paused should have correct properties`() {
        // Given
        val audioFilePath = "/test/audio.mp3"
        val currentPosition = 10000L
        val state = PlaybackState.Paused(audioFilePath, currentPosition)

        // Then
        Assert.assertEquals(audioFilePath, state.audioFilePath)
        Assert.assertEquals(currentPosition, state.currentPosition)
    }

    @Test
    fun `PlaybackState Error should have correct properties`() {
        // Given
        val errorMessage = "File not found"
        val previousState = PlaybackState.Playing("/test/audio.mp3")
        val state = PlaybackState.Error(errorMessage, previousState)

        // Then
        Assert.assertEquals(errorMessage, state.message)
        assertEquals(previousState, state.previousState)
    }

    @Test
    fun `PlaybackState Error should handle null previous state`() {
        // Given
        val errorMessage = "File not found"
        val state = PlaybackState.Error(errorMessage)

        // Then
        Assert.assertEquals(errorMessage, state.message)
        Assert.assertEquals(null, state.previousState)
    }
}
