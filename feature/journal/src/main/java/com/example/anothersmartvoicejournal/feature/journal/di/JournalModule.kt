package com.example.anothersmartvoicejournal.feature.journal.di

import com.example.anothersmartvoicejournal.feature.journal.data.repository.PlaybackRepository
import com.example.anothersmartvoicejournal.feature.journal.data.repository.PlaybackRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class JournalModule {

    @Binds
    abstract fun bindPlaybackRepository(
        playbackRepositoryImpl: PlaybackRepositoryImpl
    ): PlaybackRepository
}
