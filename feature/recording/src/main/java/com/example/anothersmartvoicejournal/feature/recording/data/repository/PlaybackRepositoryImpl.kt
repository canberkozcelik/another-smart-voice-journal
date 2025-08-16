package com.example.anothersmartvoicejournal.feature.recording.data.repository

import android.content.Context
import android.media.MediaPlayer
import javax.inject.Inject

class PlaybackRepositoryImpl @Inject constructor(
    private val context: Context
) : PlaybackRepository {
    
    private var mediaPlayer: MediaPlayer? = null
    
    override suspend fun getAudioDuration(audioFilePath: String): Long {
        return try {
            val player = MediaPlayer()
            player.setDataSource(audioFilePath)
            player.prepare()
            val duration = player.duration.toLong()
            player.release()
            duration
        } catch (e: Exception) {
            -1L
        }
    }
    
    override suspend fun startPlayback(audioFilePath: String): Result<Unit> {
        return try {
            // Input validation
            if (audioFilePath.isBlank()) {
                return Result.failure(IllegalArgumentException("Invalid file path"))
            }
            
            // Clean up existing player
            mediaPlayer?.release()
            
            // Create new player
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFilePath)
                prepare()
                start()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun pausePlayback(): Result<Unit> {
        return try {
            mediaPlayer?.pause()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun stopPlayback(): Result<Unit> {
        return try {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCurrentPosition(): Long {
        return try {
            mediaPlayer?.currentPosition?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
    
    override fun cleanup() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
