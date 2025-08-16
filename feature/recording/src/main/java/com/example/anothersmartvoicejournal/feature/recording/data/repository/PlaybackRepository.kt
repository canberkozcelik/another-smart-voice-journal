package com.example.anothersmartvoicejournal.feature.recording.data.repository

interface PlaybackRepository {
    
    /**
     * Get the duration of an audio file in milliseconds
     * @param audioFilePath Path to the audio file
     * @return Duration in milliseconds, or -1L if error occurs
     */
    suspend fun getAudioDuration(audioFilePath: String): Long
    
    /**
     * Start playback of an audio file
     * @param audioFilePath Path to the audio file
     * @return Result indicating success or failure
     */
    suspend fun startPlayback(audioFilePath: String): Result<Unit>
    
    /**
     * Pause the currently playing audio
     * @return Result indicating success or failure
     */
    suspend fun pausePlayback(): Result<Unit>
    
    /**
     * Stop the currently playing audio and reset to beginning
     * @return Result indicating success or failure
     */
    suspend fun stopPlayback(): Result<Unit>
    
    /**
     * Get the current playback position in milliseconds
     * @return Current position in milliseconds, or 0L if not playing
     */
    suspend fun getCurrentPosition(): Long
    
    /**
     * Clean up resources when repository is no longer needed
     */
    fun cleanup()
}
