package com.example.anothersmartvoicejournal.feature.recording.data.repository

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.example.anothersmartvoicejournal.feature.recording.data.model.RecordingState
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

@Singleton
class RecordingRepositoryImpl @Inject constructor(
    private val context: Context
) : RecordingRepository {

    private var mediaRecorder: MediaRecorder? = null
    private var recordingFile: File? = null
    private var startTime: Long = 0L

    private val _recordingState = MutableStateFlow(RecordingState())
    override fun getCurrentRecordingState(): StateFlow<RecordingState> = _recordingState.asStateFlow()

    private val _recordingDuration = MutableStateFlow(0L)
    override fun getRecordingDuration(): StateFlow<Long> = _recordingDuration.asStateFlow()

    override fun startRecording(): Flow<RecordingState> {
        return try {
            createRecordingFile()
            setupMediaRecorder()
            mediaRecorder?.start()
            startTime = System.currentTimeMillis()

            val startState = RecordingState(
                isRecording = true,
                duration = 0L,
                filePath = recordingFile?.absolutePath
            )
            
            // Update the StateFlows
            _recordingState.value = startState
            _recordingDuration.value = 0L

            flowOf(startState)
        } catch (e: Exception) {
            val errorState = RecordingState(
                isRecording = false,
                duration = 0L,
                error = e.message
            )
            _recordingState.value = errorState
            flowOf(errorState)
        }
    }

    override fun stopRecording(): Flow<RecordingState> {
        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            val finalDuration = System.currentTimeMillis() - startTime
            val finalState = RecordingState(
                isRecording = false,
                duration = finalDuration,
                filePath = recordingFile?.absolutePath
            )
            
            // Update the StateFlows
            _recordingState.value = finalState
            _recordingDuration.value = finalDuration

            flowOf(finalState)
        } catch (e: Exception) {
            // Ensure MediaRecorder is cleaned up even on failure
            try {
                mediaRecorder?.release()
            } catch (releaseException: Exception) {
                // Ignore release exceptions
            }
            mediaRecorder = null

            val errorState = RecordingState(
                isRecording = false,
                duration = 0L,
                error = e.message
            )
            _recordingState.value = errorState
            flowOf(errorState)
        }
    }



    override fun isRecording(): Boolean = _recordingState.value.isRecording

    override fun getRecordingFilePath(): String? = recordingFile?.absolutePath

    private fun createRecordingFile() {
        val recordingsDir = File(context.filesDir, "recordings").apply {
            if (!exists()) mkdirs()
        }
        recordingFile = File(recordingsDir, "recording_${System.currentTimeMillis()}.mp3")
    }

    private fun setupMediaRecorder() {
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFile(recordingFile?.absolutePath)
            prepare()
        }
    }
}
