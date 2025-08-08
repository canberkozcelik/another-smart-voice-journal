package com.example.anothersmartvoicejournal.feature.recording.di

import com.example.anothersmartvoicejournal.feature.recording.data.repository.RecordingRepository
import com.example.anothersmartvoicejournal.feature.recording.data.repository.RecordingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecordingModule {

    @Binds
    abstract fun bindRecordingRepository(
        recordingRepositoryImpl: RecordingRepositoryImpl
    ): RecordingRepository
}
